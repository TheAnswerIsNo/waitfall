package com.waitfall.system.controller;

import cn.dev33.satoken.util.SaResult;
import com.waitfall.framework.pojo.BaseController;
import com.waitfall.framework.pojo.PageVO;
import com.waitfall.system.domain.dto.user.*;
import com.waitfall.system.domain.vo.user.UserDetailVO;
import com.waitfall.system.domain.vo.user.UserListVO;
import com.waitfall.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/11 13:37
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户", description = "用户相关接口")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping("/list")
    public SaResult list(@ModelAttribute @ParameterObject @Validated UserListDTO userListDTO) {
        PageVO<List<UserListVO>> list = userService.list(userListDTO);
        return new SaResult(SaResult.CODE_SUCCESS,"查询成功",list);
    }

    @Operation(summary = "新增用户")
    @PostMapping("/add")
    public SaResult add(@RequestBody @Validated UserAddDTO userAddDTO) {
        userService.add(userAddDTO);
        return SaResult.ok("新增用户成功");
    }

    @Operation(summary = "修改用户")
    @PostMapping("/update")
    public SaResult update(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        userService.update(userUpdateDTO);
        return SaResult.ok("修改用户成功");
    }

    @Operation(summary = "分配角色")
    @PostMapping("/distribution/role")
    public SaResult distributionRole(@RequestBody @Validated UserDistributionRoleDTO userDistributionRoleDTO) {
        userService.distributionRole(userDistributionRoleDTO);
        return SaResult.ok("分配角色成功");
    }

    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public SaResult delete(@RequestBody @NotEmpty(message = "用户id不能为空") List<String> ids) {
        userService.delete(ids);
        return SaResult.ok("删除用户成功");
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/detail/{id}")
    public SaResult detail(@PathVariable("id") String id) {
        UserDetailVO detail = userService.detail(id);
        return SaResult.data(detail);
    }

    @Operation(summary = "修改密码")
    @PostMapping("/update/password")
    public SaResult updatePassword(@RequestBody @Validated UserUpdatePasswordDTO userUpdatePasswordDTO) {
        userService.updatePassword(userUpdatePasswordDTO);
        return SaResult.ok("修改密码成功");
    }


}
