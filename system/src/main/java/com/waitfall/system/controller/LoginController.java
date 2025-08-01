package com.waitfall.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.util.SaResult;
import com.waitfall.framework.pojo.BaseController;
import com.waitfall.system.domain.dto.user.LoginDTO;
import com.waitfall.system.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by 秋
 * @date 2025/7/8 22:43
 */
@RestController
@Tag(name = "登录", description = "登录相关接口")
public class LoginController extends BaseController {

    @Resource
    private LoginService loginService;

    @SaIgnore
    @Operation(summary = "登录")
    @PostMapping("/login")
    public SaResult login(@RequestBody @Validated LoginDTO loginDTO) {
        loginService.login(loginDTO);
        return SaResult.ok("登录成功");
    }

    @Operation(summary = "注销")
    @PostMapping("/logout")
    public SaResult logout() {
        loginService.logout();
        return SaResult.ok("注销成功");
    }

    @Operation(summary = "获取登录用户信息")
    @GetMapping("/get/info")
    public SaResult getInfo() {
        return SaResult.data(loginService.getInfo());
    }
}
