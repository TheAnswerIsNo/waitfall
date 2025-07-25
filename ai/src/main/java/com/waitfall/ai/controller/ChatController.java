package com.waitfall.ai.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @author by 秋
 * @date 2025/7/25 15:02
 */
@Slf4j
@Tag(name = "聊天", description = "聊天相关接口")
@RestController
@RequestMapping("/ai/chat")
public class ChatController {

    @Resource
    private ChatClient chatClient;

    @Operation(summary = "聊天")
    @PostMapping("/send")
    public SaResult send(@RequestParam(defaultValue = "讲个笑话") String prompt) {
        String content = chatClient
                .prompt(prompt)
                .call()
                .content();
        return SaResult.data(content);
    }

    @SaIgnore
    @Operation(summary = "聊天-流式返回")
    @GetMapping(value = "/send/stream",produces = MediaType.TEXT_HTML_VALUE)
    public Flux<String> sendStream(@RequestParam(defaultValue = "讲个笑话") String prompt) {
        return chatClient
                .prompt(prompt)
                .stream()
                .content();
    }

}
