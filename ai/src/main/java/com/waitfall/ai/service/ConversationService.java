package com.waitfall.ai.service;

import com.waitfall.ai.convert.conversation.ConversationConvert;
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

    public List<ConversationListVO> list() {
        List<TConversation> list = tConversationRepository.lambdaQuery()
                .eq(TConversation::getUserId, getUserId())
                .orderByDesc(TConversation::getTop)
                .orderByDesc(TConversation::getCreateTime)
                .list();
        return ConversationConvert.INSTANCE.parseToListVO(list);
    }

    public String add(String title) {
        TConversation tConversation = TConversation.builder()
                .userId(getUserId())
                .title(title)
                .top(false)
                .build();
        tConversationRepository.save(tConversation);
        return tConversation.getId();
    }

    public void delete(String id) {
        tConversationRepository.removeById(id);
    }

    public void rename(String id, String title) {
        tConversationRepository.lambdaUpdate().eq(TConversation::getId, id)
                .set(TConversation::getTitle, title)
                .update();
    }
}
