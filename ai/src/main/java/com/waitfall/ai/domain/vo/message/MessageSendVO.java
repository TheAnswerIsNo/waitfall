package com.waitfall.ai.domain.vo.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/8/7 20:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageSendVO {

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "会话id")
    private String conversationId;
}
