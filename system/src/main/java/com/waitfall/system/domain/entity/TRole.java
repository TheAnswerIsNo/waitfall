package com.waitfall.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.waitfall.framework.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autotable.annotation.Column;
import org.dromara.mpe.autotable.annotation.ColumnId;
import org.dromara.mpe.autotable.annotation.Table;
import org.dromara.mpe.processer.annotation.AutoRepository;

/**
 * @author by 秋
 * @date 2025/7/8 22:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_role",comment = "角色表")
@AutoRepository
public class TRole extends BaseEntity{

    @ColumnId(value = "id",comment = "ID",type = MysqlTypeConstant.VARCHAR,length = 45,mode = IdType.ASSIGN_ID)
    private String id;

    @Column(value = "name",comment = "角色名称",type = MysqlTypeConstant.VARCHAR,length = 10,notNull = true)
    private String name;

    @Index(name = "idx_tag",type = IndexTypeEnum.UNIQUE,comment = "角色标识唯一索引")
    @Column(value = "tag",comment = "角色标识",type = MysqlTypeConstant.VARCHAR,length = 10,notNull = true)
    private String tag;

    @Column(value = "sort",comment = "排序",type = MysqlTypeConstant.INT,notNull = true,defaultValue = "0")
    private Integer sort;

    @Column(value = "description",comment = "角色描述",type = MysqlTypeConstant.VARCHAR,length = 20)
    private String description;

    @Column(value = "enabled",comment = "是否启用",type = MysqlTypeConstant.TINYINT,length = 1,notNull = true,defaultValue = "1")
    private Boolean enabled;
}
