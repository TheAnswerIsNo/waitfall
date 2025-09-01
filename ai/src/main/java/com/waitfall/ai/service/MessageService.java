package com.waitfall.ai.service;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.waitfall.ai.convert.message.MessageConvert;
import com.waitfall.ai.domain.dto.message.MessageSendDTO;
import com.waitfall.ai.domain.entity.TMessage;
import com.waitfall.ai.domain.repository.TMessageRepository;
import com.waitfall.ai.domain.vo.message.MessageListVO;
import com.waitfall.ai.domain.vo.message.MessageSendVO;
import com.waitfall.framework.pojo.BaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author by 秋
 * @date 2025/7/31 14:44
 */
@Slf4j
@Service
public class MessageService extends BaseService {

    @Resource
    private ChatClient chatClient;

    @Resource
    private TMessageRepository tMessageRepository;

    @Resource
    private ConversationService conversationService;

    @Transactional
    public Flux<SaResult> sendStream(MessageSendDTO messageSendDTO) {
        String userSendMessage = messageSendDTO.getMessage();
        if (BooleanUtil.isFalse(messageSendDTO.getThink())) {
            userSendMessage = userSendMessage + "/no_think";
        }
        String conversationId = messageSendDTO.getConversationId();
        if (StrUtil.isBlank(messageSendDTO.getConversationId())) {
            conversationId = conversationService.add("新会话");
        }
        // 保存当前用户消息
        TMessage userMessage = TMessage.builder()
                .conversationId(conversationId)
                .type(MessageType.USER)
                .content(messageSendDTO.getMessage())
                .build();
        tMessageRepository.save(userMessage);
        // 保存AI消息
        TMessage assistantMessage = TMessage.builder()
                .replyId(userMessage.getId())
                .conversationId(conversationId)
                .type(MessageType.ASSISTANT)
                .build();
        tMessageRepository.save(assistantMessage);
        // 构建会话记忆
        List<Message> historyMessageList = buildHistoryMessageList(messageSendDTO, conversationId);
        Prompt prompt = new Prompt(historyMessageList);

        // 拿到AI响应
        StringBuffer stringBuffer = new StringBuffer();
        String finalConversationId = conversationId;

        return chatClient
                .prompt(prompt)
                .user(userSendMessage)
                .stream()
                .chatResponse().map(chunk -> {
                    String newContent = ObjUtil.isNotNull(chunk.getResult()) ? chunk.getResult().getOutput().getText() : StrUtil.EMPTY;
                    stringBuffer.append(newContent);
                    return SaResult.data(MessageSendVO.builder().conversationId(finalConversationId).content(newContent).build());
                })
                .doOnComplete(() -> {
                    // 保存对话消息
                    assistantMessage.setContent(stringBuffer.toString());
                    tMessageRepository.updateById(assistantMessage);
                    // 异步修改会话名称
                    // 提取会话标题并保存 只在第一次新对话时更新title
                    if (StrUtil.isBlank(messageSendDTO.getConversationId())) {
                        CompletableFuture.runAsync(() -> getAndSaveTitleAsync(messageSendDTO.getMessage(), stringBuffer.toString(), finalConversationId));
                    }
                })
                .doOnError(throwable -> {
                    log.error("[sendStream][message({}) 发生异常]", messageSendDTO.getMessage(), throwable);
                    // 保存异常消息
                    assistantMessage.setContent("对话生成异常，请稍后重试！");
                    tMessageRepository.updateById(assistantMessage);
                })
                .onErrorResume(error -> Flux.just(SaResult.error("对话生成异常，请稍后重试！")));
    }

    private void getAndSaveTitleAsync(String userMessage, String content, String conversationId) {
        String promptTemplate = """
                你是一个高度精确的指令提取器。你的唯一任务是从用户的发言中，用一句话总结出最核心的 **指令** 或 **问题**。
                
                # 核心原则:
                你的总结必须是用户行为的直接反映，而不是对用户意图的解读。
                
                # 要求:
                1.  **优先提取指令 (最重要)**:
                 *   当用户的发言是一个明确的指令时（如“解释代码”、“翻译文本”、“写一首诗”），你的总结 **必须** 是该指令的直接、简洁形式（如“解释代码”）。
                 *   **严禁** 将指令转述为用户的“需求”或“目的”（例如，绝不能将“解释代码”总结为“我想理解这段代码”或“用户需要代码解释”）。

                2.  **聚焦用户，忽略AI**:
                 *   你的总结必须 **完全且仅** 基于用户的输入。
                 *   AI的回答仅供你理解上下文，总结中 **绝对不能包含** 任何由AI生成的信息。
                
                3.  **提炼核心问题**:
                 *   如果用户的发言不是指令而是一个问题，则精准地提炼出这个核心问题。
                 *   问题必须是用户直接提出的，不能是用户的解释或推理。

                4.  **输出格式**:
                 *   直接返回总结后的那句话，不要有任何引言、标题或多余的文字。
                
                # 对话记录:
                ""\"
                user: %s
                assistant: %s
                ""\"
                """.formatted(userMessage, content);
        Prompt prompt = new Prompt(promptTemplate);
        // 构造指定参数
        ChatOptions chatOptions = ChatOptions.builder().maxTokens(15).build();
        String title = chatClient.prompt(prompt).options(chatOptions).messages(new UserMessage("/no_think")).call().content();
        String finalTile = StrUtil.isNotBlank(title) ? title.replace("<think>", "").replace("</think>", "").trim() : "新会话";
        log.info("会话标题:{}", finalTile);
        conversationService.rename(conversationId, finalTile);
    }

    private List<Message> buildHistoryMessageList(MessageSendDTO messageSendDTO, String conversationId) {
        // 查询历史记录
        List<TMessage> list = tMessageRepository.lambdaQuery()
                .eq(TMessage::getConversationId, conversationId)
                .orderByAsc(TMessage::getId)
                .list();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        List<TMessage> historyMessageList = new ArrayList<>(messageSendDTO.getMaxMessages());
        for (int i = list.size() - 1; i >= 0; i--) {
            TMessage assistantMessage = CollUtil.get(list, i);
            if (ObjUtil.isNull(assistantMessage) || StrUtil.isBlank(assistantMessage.getReplyId())) {
                continue;
            }
            TMessage userMessage = CollUtil.get(list, i - 1);
            if (ObjUtil.isNull(userMessage)
                    || ObjUtil.notEqual(assistantMessage.getReplyId(), userMessage.getId())
                    || StrUtil.isBlank(assistantMessage.getContent())) {
                continue;
            }
            // 由于后续要 reverse 反转，所以先添加 assistantMessage
            historyMessageList.add(assistantMessage);
            historyMessageList.add(userMessage);
            // 超过最大上下文，结束
            if (historyMessageList.size() >= messageSendDTO.getMaxMessages()) {
                break;
            }
        }

        // 反转列表
        Collections.reverse(historyMessageList);
        // 构建messageList
        List<Message> messageList = new ArrayList<>();
        for (TMessage tMessage : historyMessageList) {
            // 一个userMessage和一个assistantMessage为一组 根据replyId
            // 根据消息类型创建对应的消息对象
            switch (tMessage.getType()) {
                case USER -> messageList.add(new UserMessage(tMessage.getContent()));
                case ASSISTANT -> messageList.add(new AssistantMessage(tMessage.getContent()));
            }
        }
        return messageList;
    }

    public List<MessageListVO> list(String conversationId) {
        List<TMessage> list = tMessageRepository.lambdaQuery()
                .eq(TMessage::getConversationId, conversationId)
                .orderByAsc(TMessage::getId)
                .list();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }

        return MessageConvert.INSTANCE.parseToListVO(list);
    }
}
