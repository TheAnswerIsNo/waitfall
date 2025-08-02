package com.waitfall.ai.domain.vo.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/8/2 1:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageListVO {

    @Schema(description = "消息ID")
    private String id;

    @Schema(description = "消息内容")
    private String content;


}
