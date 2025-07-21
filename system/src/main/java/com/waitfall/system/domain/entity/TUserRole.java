package com.waitfall.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autotable.annotation.Column;
import org.dromara.mpe.autotable.annotation.ColumnId;
import org.dromara.mpe.autotable.annotation.Table;
import org.dromara.mpe.processer.annotation.AutoRepository;

/**
 * @author by 秋
 * @date 2025/7/8 22:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_user_role",comment = "用户角色关联表")
@AutoRepository
public class TUserRole{

    @ColumnId(value = "id",comment = "ID",type = MysqlTypeConstant.VARCHAR,length = 45,mode = IdType.ASSIGN_ID)
    private String id;

    @Column(value = "user_id",comment = "用户ID",type = MysqlTypeConstant.VARCHAR,length = 45,notNull = true)
    private String userId;

    @Column(value = "role_id",comment = "角色ID",type = MysqlTypeConstant.VARCHAR,length = 45,notNull = true)
    private String roleId;
}
