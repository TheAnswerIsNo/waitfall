package com.waitfall.ai.convert.conversation;

import com.waitfall.ai.domain.entity.TConversation;
import com.waitfall.ai.domain.vo.conversation.ConversationListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/8/1 20:30
 */
@Mapper
public interface ConversationConvert {

    ConversationConvert INSTANCE = Mappers.getMapper(ConversationConvert.class);

    List<ConversationListVO> parseToListVO(List<TConversation> tConversationList);

}
