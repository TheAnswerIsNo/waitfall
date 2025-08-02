package com.waitfall.ai.service;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.waitfall.ai.domain.dto.message.MessageSendDTO;
import com.waitfall.ai.domain.entity.TMessage;
import com.waitfall.ai.domain.repository.TMessageRepository;
import com.waitfall.ai.domain.vo.message.MessageListVO;
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

    @Transactional
    public Flux<SaResult> sendStream(MessageSendDTO messageSendDTO) {
        // 保存当前用户消息
        TMessage userMessage = TMessage.builder()
                .conversationId(messageSendDTO.getConversationId())
                .messageType(MessageType.USER.getValue())
                .content(messageSendDTO.getMessage())
                .build();
        tMessageRepository.save(userMessage);
        // 保存AI消息
        TMessage assistantMessage = TMessage.builder()
                .replyId(userMessage.getId())
                .conversationId(messageSendDTO.getConversationId())
                .messageType(MessageType.ASSISTANT.getValue())
                .build();
        tMessageRepository.save(assistantMessage);
        // 构建会话记忆
        List<Message> historyMessageList = buildHistoryMessageList(messageSendDTO);
        Prompt prompt = new Prompt(historyMessageList);

        // 拿到AI响应
        StringBuffer stringBuffer = new StringBuffer();
        return chatClient
                .prompt(prompt)
                .user(messageSendDTO.getMessage() + "/no_think")
                .stream()
                .chatResponse().map(chunk -> {
                    String newContent = ObjUtil.isNotNull(chunk.getResult()) ? chunk.getResult().getOutput().getText() : StrUtil.EMPTY;
                    stringBuffer.append(newContent);
                    return SaResult.data(newContent);
                })
                .doOnComplete(()->{
                    // 保存对话消息
                    assistantMessage.setContent(stringBuffer.toString());
                    tMessageRepository.updateById(assistantMessage);
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
        // 查询历史记录 仅保留十组对话 TODO 后续改为配置
        List<TMessage> list = tMessageRepository.lambdaQuery()
                .eq(TMessage::getConversationId, messageSendDTO.getConversationId())
                .orderByAsc(TMessage::getCreateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        List<TMessage> historyMessageList = new ArrayList<>(10 * 2);
        for (int i = list.size() - 1; i >= 0; i--) {
            TMessage assistantMessage = CollUtil.get(list, i);
            if (assistantMessage == null || assistantMessage.getReplyId() == null) {
                continue;
            }
            TMessage userMessage = CollUtil.get(list, i - 1);
            if (userMessage == null
                    || ObjUtil.notEqual(assistantMessage.getReplyId(), userMessage.getId())
                    || StrUtil.isEmpty(assistantMessage.getContent())) {
                continue;
            }
            // 由于后续要 reverse 反转，所以先添加 assistantMessage
            historyMessageList.add(assistantMessage);
            historyMessageList.add(userMessage);
            // 超过最大上下文，结束
            if (historyMessageList.size() >= 10 * 2) {
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
            if (MessageType.USER.getValue().equals(tMessage.getMessageType())) {
                messageList.add(new UserMessage(tMessage.getContent()));
            } else if (MessageType.ASSISTANT.getValue().equals(tMessage.getMessageType())) {
                messageList.add(new AssistantMessage(tMessage.getContent()));
            }
        }
        return messageList;
    }

    public List<MessageListVO> list(String conversationId) {
        List<TMessage> list = tMessageRepository.lambdaQuery()
                .eq(TMessage::getConversationId, conversationId)
                .orderByDesc(TMessage::getCreateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        List<MessageListVO> messageListVOList = new ArrayList<>();
        for (TMessage tMessage : list) {
            messageListVOList.add(new MessageListVO(tMessage.getId(), tMessage.getContent()));
        }
        return messageListVOList;
    }
}
