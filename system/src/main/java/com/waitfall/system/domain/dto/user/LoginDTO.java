package com.waitfall.system.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/7/11 9:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    @Schema(description = "账号")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;
}
