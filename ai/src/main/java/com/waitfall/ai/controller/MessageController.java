package com.waitfall.ai.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import com.waitfall.ai.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author by 秋
 * @date 2025/7/25 15:02
 */
@Slf4j
@Tag(name = "聊天消息", description = "聊天消息相关接口")
@RestController
@RequestMapping("/ai/message")
public class MessageController {

    @Resource
    private ChatClient chatClient;

    @Resource
    private MessageService messageService;

    @Operation(summary = "发送消息")
    @PostMapping("/send")
    public SaResult send(@RequestParam(defaultValue = "讲个笑话") String prompt) {
        String content = chatClient
                .prompt(new Prompt())
                .user(prompt)
                .call()
                .content();
        return SaResult.data(content);
    }

    @SaIgnore
    @Operation(summary = "发送消息-流式")
    @PostMapping(value = "/send/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SaResult> sendStream(@RequestParam(defaultValue = "讲个笑话") String message) {
        return messageService.sendStream(message);
    }

}
