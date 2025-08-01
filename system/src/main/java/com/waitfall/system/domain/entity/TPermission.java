package com.waitfall.system.domain.entity;

import com.waitfall.framework.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.autotable.annotation.enums.DefaultValueEnum;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autotable.annotation.Column;
import org.dromara.mpe.autotable.annotation.ColumnId;
import org.dromara.mpe.autotable.annotation.Table;
import org.dromara.mpe.processer.annotation.AutoRepository;

/**
 * @author by 秋
 * @date 2025/7/8 22:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_permission",comment = "权限表")
@AutoRepository
public class TPermission extends BaseEntity{

    @ColumnId(value = "id",comment = "ID",type = MysqlTypeConstant.VARCHAR,length = 45)
    private String id;

    @Column(value = "parent_id",comment = "父级ID",type = MysqlTypeConstant.VARCHAR,length = 45,notNull = true,defaultValueType = DefaultValueEnum.EMPTY_STRING)
    private String parentId;

    @Column(value = "name",comment = "权限名称",type = MysqlTypeConstant.VARCHAR,length = 10,notNull = true)
    private String name;

    @Column(value = "tag",comment = "权限标识",type = MysqlTypeConstant.VARCHAR,length = 30,notNull = true)
    private String tag;

    @Column(value = "type",comment = "权限类型",type = MysqlTypeConstant.VARCHAR,length = 10,notNull = true)
    private String type;

    @Column(value = "sort",comment = "排序",type = MysqlTypeConstant.INT,notNull = true,defaultValue = "0")
    private Integer sort;

    @Column(value = "description",comment = "权限描述",type = MysqlTypeConstant.VARCHAR,length = 20)
    private String description;

    @Column(value = "enabled",comment = "是否启用",type = MysqlTypeConstant.TINYINT,length = 1,notNull = true,defaultValue = "1")
    private Boolean enabled;
}
