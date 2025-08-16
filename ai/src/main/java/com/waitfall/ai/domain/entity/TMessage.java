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
import org.springframework.ai.chat.messages.MessageType;

/**
 * @author by 秋
 * @date 2025/8/1 13:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "t_message",comment = "聊天消息表")
@AutoRepository
public class TMessage extends BaseEntity {

    @ColumnId(value = "id",comment = "ID",type = MysqlTypeConstant.VARCHAR,length = 45)
    private String id;

    @Column(value = "reply_id",comment = "回复ID",type = MysqlTypeConstant.VARCHAR,length = 45)
    private String replyId;

    @Column(value = "conversation_id",comment = "会话ID",type = MysqlTypeConstant.VARCHAR,length = 45,notNull = true)
    private String conversationId;

    @Column(value = "type",comment = "消息类型",type = MysqlTypeConstant.ENUM,length = 45, notNull = true)
    private MessageType type;

    @Column(value = "content",comment = "消息内容",type = MysqlTypeConstant.LONGTEXT)
    private String content;
}
