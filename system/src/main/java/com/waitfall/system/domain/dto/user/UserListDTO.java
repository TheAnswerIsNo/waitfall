package com.waitfall.system.domain.dto.user;

import com.waitfall.framework.pojo.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by 秋
 * @date 2025/7/14 17:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO extends PageDTO {

    @Schema(description = "昵称")
    private String nickname;
}
