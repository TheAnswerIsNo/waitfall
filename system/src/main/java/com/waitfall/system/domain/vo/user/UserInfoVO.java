package com.waitfall.system.domain.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author by 秋
 * @date 2025/7/7 23:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "单位ID")
    private String unitId;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
