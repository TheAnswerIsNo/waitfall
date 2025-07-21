package com.waitfall.system.domain.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/15 13:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "单位ID")
    private String unitId;

    @Schema(description = "角色ID")
    private List<String> roleIds;
}
