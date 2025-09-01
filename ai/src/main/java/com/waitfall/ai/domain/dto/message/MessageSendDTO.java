package com.waitfall.ai.domain.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String conversationId;

    @Schema(description = "消息内容")
    @NotBlank(message = "消息内容不能为空")
    private String message;

    @Schema(description = "是否开启思考")
    @NotNull(message = "是否思考不能为空")
    private Boolean think;

    @Schema(description = "最大消息数")
    @NotNull(message = "最大消息数不能为空")
    @Max(value = 20, message = "最大消息数不能超过20")
    private Integer maxMessages;
}
