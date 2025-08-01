package com.waitfall.ai.domain.vo.conversation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/8/1 20:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationListVO {

    @Schema(description = "会话ID")
    private String id;

    @Schema(description = "会话标题")
    private String title;
}
