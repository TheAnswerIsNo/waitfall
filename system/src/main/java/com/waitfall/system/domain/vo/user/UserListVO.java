package com.waitfall.system.domain.vo.user;

import com.waitfall.system.domain.entity.TUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;
import org.dromara.core.trans.vo.VO;

import java.time.LocalDateTime;

/**
 * @author by 秋
 * @date 2025/7/14 17:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListVO implements VO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "单位ID")
    private String unitId;

    @Schema(description = "单位名称")
    private String unitName;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "创建人")
    @Trans(type = TransType.SIMPLE,target = TUser.class,fields = "nickname",refs = "createdByName")
    private String createdBy;

    @Schema(description = "创建人名称")
    private String createdByName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
