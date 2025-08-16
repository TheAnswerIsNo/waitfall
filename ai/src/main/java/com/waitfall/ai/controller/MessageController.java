package com.waitfall.ai.controller;

import cn.dev33.satoken.util.SaResult;
import com.waitfall.ai.domain.dto.message.MessageSendDTO;
import com.waitfall.ai.domain.vo.message.MessageListVO;
import com.waitfall.ai.service.MessageService;
import com.waitfall.framework.pojo.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/25 15:02
 */
@Slf4j
@Tag(name = "聊天消息", description = "聊天消息相关接口")
@RestController
@RequestMapping("/ai/message")
public class MessageController extends BaseController {

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

    @Operation(summary = "发送消息-流式")
    @PostMapping(value = "/send/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SaResult> sendStream(@RequestBody @Validated MessageSendDTO  messageSendDTO, HttpServletResponse httpServletResponse) {
        // 设置响应头，防止sse失败
        httpServletResponse.setHeader("Cache-Control", "no-cache, no-transform");
        return messageService.sendStream(messageSendDTO);
    }

    @Operation(summary = "获取会话消息")
    @GetMapping(value = "/list/{conversationId}")
    public SaResult list(@PathVariable("conversationId") String conversationId) {
        List<MessageListVO> list = messageService.list(conversationId);
        return SaResult.data(list);
    }


}
