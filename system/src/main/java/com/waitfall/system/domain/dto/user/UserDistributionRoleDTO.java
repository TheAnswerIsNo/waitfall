package com.waitfall.system.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/16 16:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDistributionRoleDTO {

    @NotBlank(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private String userId;

    @NotEmpty(message = "角色ID列表不能为空")
    @Schema(description = "角色ID列表")
    private List<String> roleIds;
}
