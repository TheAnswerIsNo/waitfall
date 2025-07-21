package com.waitfall.system.domain.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/14 16:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionListVO{

    @Schema(description = "ID")
    private String id;

    @Schema(description = "父级ID")
    private String parentId;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限标识")
    private String tag;

    @Schema(description = "权限类型")
    private String type;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "权限描述")
    private String description;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "子级权限")
    private List<PermissionListVO> children;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
