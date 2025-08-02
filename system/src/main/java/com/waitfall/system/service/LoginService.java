package com.waitfall.system.service;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.waitfall.framework.pojo.BaseService;
import com.waitfall.framework.pojo.CommonException;
import com.waitfall.system.domain.dto.user.LoginDTO;
import com.waitfall.system.domain.entity.TUser;
import com.waitfall.system.domain.vo.user.UserInfoVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by 秋
 * @date 2025/7/8 22:44
 */
@Slf4j
@Service
public class LoginService extends BaseService {

    @Resource
    private UserService userService;

    public void login(LoginDTO loginDTO) {

        TUser userByAccount = userService.getUserByAccount(loginDTO.getAccount());
        // 校验账号是否存在
        if (ObjUtil.isEmpty(userByAccount)) {
            throw new CommonException(400,"账号不存在");
        }
        // 校验密码是否正确
        if (!BCrypt.checkpw(loginDTO.getPassword(), userByAccount.getPassword())) {
            throw new SaTokenException(400,"密码错误");
        }

        publicLogin(userByAccount.getId());
    }

    private void publicLogin(String userId) {
        StpUtil.login(userId);
    }

    public void logout() {
        StpUtil.logout();
    }

    public UserInfoVO getInfo() {
        return userService.getUserInfo(getUserId());
    }
}
