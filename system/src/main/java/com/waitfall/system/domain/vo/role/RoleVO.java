package com.waitfall.system.domain.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author by 秋
 * @date 2025/7/14 16:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色标识")
    private String tag;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
