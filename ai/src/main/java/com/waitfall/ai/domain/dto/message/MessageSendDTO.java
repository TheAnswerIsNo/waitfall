package com.waitfall.ai.domain.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/8/1 20:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendDTO {

    @Schema(description = "会话ID")
    @NotBlank(message = "会话ID不能为空")
    private String conversationId;

    @Schema(description = "消息内容")
    @NotBlank(message = "消息内容不能为空")
    private String message;
}
