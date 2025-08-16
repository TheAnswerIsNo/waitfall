package com.waitfall.ai.convert.message;

import com.waitfall.ai.domain.entity.TMessage;
import com.waitfall.ai.domain.vo.message.MessageListVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/8/1 20:30
 */
@Mapper
public interface MessageConvert {

    MessageConvert INSTANCE = Mappers.getMapper(MessageConvert.class);

    @Mapping(target = "type",source = "type.value")
    MessageListVO parseToVO(TMessage tMessage);

    List<MessageListVO> parseToListVO(List<TMessage> tMessageList);
}