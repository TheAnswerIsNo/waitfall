package com.waitfall.ai.service;

import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.collection.CollUtil;
import com.waitfall.ai.domain.dto.message.MessageSendDTO;
import com.waitfall.ai.domain.entity.TMessage;
import com.waitfall.ai.domain.repository.TMessageRepository;
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

    public Flux<SaResult> sendStream(MessageSendDTO messageSendDTO) {
        // 保存当前用户消息
        TMessage userMessage = TMessage.builder()
                .conversationId(messageSendDTO.getConversationId())
                .messageType(MessageType.USER.getValue())
                .content(messageSendDTO.getMessage())
                .build();
        tMessageRepository.save(userMessage);
        // 构建会话记忆
        List<Message> historyMessageList = buildHistoryMessageList(messageSendDTO);
        Prompt prompt = new Prompt(historyMessageList);

        StringBuffer stringBuffer = new StringBuffer();
        return chatClient
                .prompt(prompt)
                .user(messageSendDTO.getMessage())
                .stream()
                .chatResponse().map(chunk -> {
                    stringBuffer.append(chunk.getResult().getOutput().getText());
                    return SaResult.data(chunk.getResult().getOutput().getText());
                })
                .doOnComplete(()->{
                    // 保存对话消息
                    TMessage assistantMessage = TMessage.builder()
                            .conversationId(messageSendDTO.getConversationId())
                            .messageType(MessageType.ASSISTANT.getValue())
                            .content(stringBuffer.toString())
                            .build();
                    tMessageRepository.save(assistantMessage);
                })
                .doOnError(throwable -> {
                    log.error("[sendStream][userId({}) message({}) 发生异常]", getUserId(), messageSendDTO.getMessage(), throwable);
                    // 保存异常消息
                    TMessage assistantMessage = TMessage.builder()
                            .conversationId(messageSendDTO.getConversationId())
                            .messageType(MessageType.ASSISTANT.getValue())
                            .content("AI回答出错，请稍后重试！")
                            .build();
                    tMessageRepository.save(assistantMessage);
                })
                .onErrorResume(error -> Flux.just(SaResult.error("AI回答出错，请稍后重试！")));
    }

    private List<Message> buildHistoryMessageList(MessageSendDTO messageSendDTO) {
        // 查询历史记录 仅保留十组对话 TODO 后续改为配置
        List<TMessage> list = tMessageRepository.lambdaQuery()
                .eq(TMessage::getConversationId, messageSendDTO.getConversationId())
                .orderByDesc(TMessage::getCreateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        List<Message> historyMessageList = new ArrayList<>(10 * 2);
        for (TMessage tMessage : list) {
            // 一个userMessage和一个assistantMessage为一组 根据replyId
            // 根据消息类型创建对应的消息对象
            if (MessageType.USER.getValue().equals(tMessage.getMessageType())) {
                historyMessageList.add(new UserMessage(tMessage.getContent()));
            } else if (MessageType.ASSISTANT.getValue().equals(tMessage.getMessageType())) {
                historyMessageList.add(new AssistantMessage(tMessage.getContent()));
            }
        }
        // 反转列表
        Collections.reverse(historyMessageList);
        return historyMessageList;
    }
}
