package com.waitfall.ai.domain.entity;

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
 * @date 2025/8/1 20:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_conversation",comment = "会话表")
@AutoRepository
public class TConversation extends BaseEntity {

    @ColumnId(value = "id",comment = "ID",type = MysqlTypeConstant.VARCHAR,length = 45)
    private String id;

    @Column(value = "user_id",comment = "用户ID",type = MysqlTypeConstant.VARCHAR,length = 45)
    private String userId;

    @Column(value = "title",comment = "会话标题",type = MysqlTypeConstant.VARCHAR,length = 45)
    private String title;
}
