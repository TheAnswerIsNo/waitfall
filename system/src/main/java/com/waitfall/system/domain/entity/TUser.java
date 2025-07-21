package com.waitfall.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.waitfall.framework.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.core.trans.vo.TransPojo;
import org.dromara.mpe.autotable.annotation.Column;
import org.dromara.mpe.autotable.annotation.ColumnId;
import org.dromara.mpe.autotable.annotation.Table;
import org.dromara.mpe.processer.annotation.AutoRepository;

/**
 * @author by 秋
 * @date 2025/7/6 2:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_user",comment = "用户表")
@AutoRepository
public class TUser extends BaseEntity implements TransPojo {

    @ColumnId(value = "id",comment = "ID",type = MysqlTypeConstant.VARCHAR,length = 45,mode = IdType.ASSIGN_ID)
    private String id;

    @Column(value = "avatar",comment = "头像",type = MysqlTypeConstant.VARCHAR,length = 512)
    private String avatar;

    @Column(value = "nickname",comment = "昵称",type = MysqlTypeConstant.VARCHAR,length = 10)
    private String nickname;

    @Column(value = "account",comment = "账号",type = MysqlTypeConstant.VARCHAR,length = 11,notNull = true)
    private String account;

    @Column(value = "password",comment = "密码",type = MysqlTypeConstant.VARCHAR,length = 100,notNull = true)
    private String password;

    @Column(value = "unit_id",comment = "单位ID",type = MysqlTypeConstant.VARCHAR,length = 45,notNull = true)
    private String unitId;

    @Column(value = "enabled",comment = "是否启用",type = MysqlTypeConstant.TINYINT,length = 1,notNull = true,defaultValue = "1")
    private Boolean enabled;
}
