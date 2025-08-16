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
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if (BooleanUtil.isFalse(messageSendDTO.getThink())){
            userSendMessage = userSendMessage + "/no_think";
        }
        if (StrUtil.isBlank(messageSendDTO.getConversationId())){
            messageSendDTO.setConversationId(conversationService.add());
        }
        // 保存当前用户消息
        TMessage userMessage = TMessage.builder()
                .conversationId(messageSendDTO.getConversationId())
                .type(MessageType.USER)
                .content(messageSendDTO.getMessage())
                .build();
        tMessageRepository.save(userMessage);
        // 保存AI消息
        TMessage assistantMessage = TMessage.builder()
                .replyId(userMessage.getId())
                .conversationId(messageSendDTO.getConversationId())
                .type(MessageType.ASSISTANT)
                .build();
        tMessageRepository.save(assistantMessage);
        // 构建会话记忆
        List<Message> historyMessageList = buildHistoryMessageList(messageSendDTO);
        Prompt prompt = new Prompt(historyMessageList);

        // 拿到AI响应
        StringBuffer stringBuffer = new StringBuffer();
        return chatClient
                .prompt(prompt)
                .user(userSendMessage)
                .stream()
                .chatResponse().map(chunk -> {
                    String newContent = ObjUtil.isNotNull(chunk.getResult()) ? chunk.getResult().getOutput().getText() : StrUtil.EMPTY;
                    stringBuffer.append(newContent);
                    return SaResult.data(MessageSendVO.builder().conversationId(messageSendDTO.getConversationId()).content(newContent).build());
                })
                .doOnComplete(()->{
                    // 保存对话消息
                    assistantMessage.setContent(stringBuffer.toString());
                    tMessageRepository.updateById(assistantMessage);
                    // TODO 修改会话名称
                })
                .doOnError(throwable -> {
                    log.error("[sendStream][message({}) 发生异常]", messageSendDTO.getMessage(), throwable);
                    // 保存异常消息
                    assistantMessage.setContent("对话生成异常，请稍后重试！");
                    tMessageRepository.updateById(assistantMessage);
                })
                .onErrorResume(error -> Flux.just(SaResult.error("对话生成异常，请稍后重试！")));
    }

    private List<Message> buildHistoryMessageList(MessageSendDTO messageSendDTO) {
        // 查询历史记录
        List<TMessage> list = tMessageRepository.lambdaQuery()
                .eq(TMessage::getConversationId, messageSendDTO.getConversationId())
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
                case USER:
                    messageList.add(new UserMessage(tMessage.getContent()));
                    break;
                case ASSISTANT:
                    messageList.add(new AssistantMessage(tMessage.getContent()));
                    break;
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
