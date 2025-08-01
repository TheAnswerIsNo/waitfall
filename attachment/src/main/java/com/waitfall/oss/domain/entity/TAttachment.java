package com.waitfall.oss.domain.entity;

import com.waitfall.framework.pojo.BaseEntity;
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
 * @date 2025/7/25 18:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_attachment",comment = "附件表")
@AutoRepository
public class TAttachment extends BaseEntity {

    @ColumnId(value = "id",comment = "ID",type = MysqlTypeConstant.VARCHAR,length = 45)
    private String id;

    @Column(value = "name",comment = "文件名称",type = MysqlTypeConstant.VARCHAR,length = 50,notNull = true)
    private String name;

    @Column(value = "object_name",comment = "oss存储唯一值",type = MysqlTypeConstant.VARCHAR,length = 50,notNull = true)
    private String objectName;

    @Column(value = "object_id",comment = "关联表id",type = MysqlTypeConstant.VARCHAR,length = 45,notNull = true)
    private String objectId;

    @Column(value = "url",comment = "文件路径",type = MysqlTypeConstant.VARCHAR,length = 255,notNull = true)
    private String url;

    @Column(value = "size",comment = "文件大小",type = MysqlTypeConstant.BIGINT,notNull = true)
    private Long size;

    @Column(value = "content_type",comment = "文件类型",type = MysqlTypeConstant.VARCHAR,length = 20,notNull = true)
    private String contentType;

    @Column(value = "suffix",comment = "文件后缀",type = MysqlTypeConstant.VARCHAR,length = 10,notNull = true)
    private String suffix;

}
