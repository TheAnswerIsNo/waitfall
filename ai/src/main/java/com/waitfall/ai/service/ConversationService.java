package com.waitfall.ai.service;

import com.waitfall.ai.convert.conversation.ConversationConvert;
import com.waitfall.ai.domain.dto.conversation.ConversationAddDTO;
import com.waitfall.ai.domain.dto.conversation.ConversationRenameDTO;
import com.waitfall.ai.domain.entity.TConversation;
import com.waitfall.ai.domain.repository.TConversationRepository;
import com.waitfall.ai.domain.vo.conversation.ConversationListVO;
import com.waitfall.framework.pojo.BaseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/8/1 20:26
 */
@Service
public class ConversationService extends BaseService {

    @Resource
    private TConversationRepository tConversationRepository;

    public List<ConversationListVO> list(String userId) {
        List<TConversation> list = tConversationRepository.lambdaQuery().eq(TConversation::getUserId, userId).list();
        return ConversationConvert.INSTANCE.parseToListVO(list);
    }

    public void add(ConversationAddDTO conversationAddDTO) {
        TConversation tConversation = ConversationConvert.INSTANCE.parseAddToEntity(conversationAddDTO, getUserId());
        tConversationRepository.save(tConversation);
    }

    public void delete(String id) {
        tConversationRepository.removeById(id);
    }

    public void rename(ConversationRenameDTO conversationRenameDTO) {
        TConversation tConversation = ConversationConvert.INSTANCE.parseRenameToEntity(conversationRenameDTO);
        tConversationRepository.updateById(tConversation);
    }
}
