package com.waitfall.ai.controller;

import cn.dev33.satoken.util.SaResult;
import com.waitfall.ai.domain.dto.conversation.ConversationRenameDTO;
import com.waitfall.ai.domain.vo.conversation.ConversationListVO;
import com.waitfall.ai.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/26 23:17
 */
@Tag(name = "会话管理", description = "会话相关接口")
@RestController
@RequestMapping("/ai/conversation")
public class ConversationController {

    @Resource
    private ConversationService conversationService;

    @Operation(summary = "获取会话列表")
    @GetMapping("/list")
    public SaResult list() {
        List<ConversationListVO> list = conversationService.list();
        return SaResult.data(list);
    }

    @Operation(summary = "添加会话")
    @PostMapping("/add")
    public SaResult add() {
        String id = conversationService.add();
        return SaResult.data(id);
    }

    @Operation(summary = "删除会话")
    @DeleteMapping("/delete")
    public SaResult delete(@RequestParam("id") String id) {
        conversationService.delete(id);
        return SaResult.ok("删除成功");
    }

    @Operation(summary = "重命名会话")
    @PostMapping("/rename")
    public SaResult rename(@RequestBody @Validated ConversationRenameDTO conversationRenameDTO) {
        conversationService.rename(conversationRenameDTO);
        return SaResult.ok("重命名成功");
    }
    
}
