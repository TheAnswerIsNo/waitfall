package com.waitfall.ai.domain.dto.conversation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/8/1 20:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationAddDTO {

    @Schema(description = "会话标题")
    @NotBlank(message = "会话标题不能为空")
    private String title;
}
