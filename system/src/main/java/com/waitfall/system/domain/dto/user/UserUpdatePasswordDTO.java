package com.waitfall.system.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/7/15 13:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordDTO {

    @NotBlank(message = "ID不能为空")
    @Schema(description = "ID")
    private String id;

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;
}
