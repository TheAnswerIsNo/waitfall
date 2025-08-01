package com.waitfall.oss.domain.vo.minio;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/8/1 9:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OssUploadVO {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "oss存储唯一值")
    private String objectName;

    @Schema(description = "文件路径")
    private String url;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件类型")
    private String contentType;

    @Schema(description = "文件后缀")
    private String suffix;
}
