package com.waitfall.system.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/15 13:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @NotBlank(message = "ID不能为空")
    @Schema(description = "ID")
    private String id;

    @NotBlank(message = "昵称不能为空")
    @Schema(description = "昵称")
    private String nickname;

    @NotBlank(message = "单位不能为空")
    @Schema(description = "单位ID")
    private String unitId;

    @NotNull(message = "启用状态不能为空")
    @Schema(description = "是否启用")
    private Boolean enabled;

    @NotEmpty(message = "角色不能为空")
    @Schema(description = "角色ID")
    private List<String> roleIds;
}
