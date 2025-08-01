package com.waitfall.ai.service;

import cn.dev33.satoken.util.SaResult;
import com.waitfall.framework.pojo.BaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author by 秋
 * @date 2025/7/31 14:44
 */
@Slf4j
@Service
public class MessageService extends BaseService {

    @Resource
    private ChatClient chatClient;

    public Flux<SaResult> sendStream(String message) {
        return chatClient
                .prompt()
                .user(message)
                .stream()
                .chatResponse().map(chunk -> SaResult.ok(chunk.getResult().getOutput().getText()))
                .doOnError(throwable -> log.error("[sendStream][userId({}) message({}) 发生异常]", getUserId(), message, throwable))
                .onErrorResume(error -> Flux.just(SaResult.error("AI回答出错，请稍后重试！")));
    }
}
