package com.waitfall.oss.domain.vo.attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author by 秋
 * @date 2025/7/31 9:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "关联表id")
    private String objectId;

    @Schema(description = "文件路径")
    private String url;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件类型")
    private String contentType;

    @Schema(description = "文件后缀")
    private String suffix;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    private LocalDate createTime;
}
