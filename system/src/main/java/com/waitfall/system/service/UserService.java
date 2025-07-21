package com.waitfall.system.service;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.waitfall.framework.pojo.BaseEntity;
import com.waitfall.framework.pojo.PageVO;
import com.waitfall.system.convert.user.UserConvert;
import com.waitfall.system.domain.dto.user.*;
import com.waitfall.system.domain.entity.TUser;
import com.waitfall.system.domain.repository.TUserRepository;
import com.waitfall.system.domain.vo.role.RoleVO;
import com.waitfall.system.domain.vo.user.UserDetailVO;
import com.waitfall.system.domain.vo.user.UserInfoVO;
import com.waitfall.system.domain.vo.user.UserListVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/7 23:56
 */
@Service
public class UserService {

    @Resource
    private TUserRepository tUserRepository;

    @Resource
    private RoleService roleService;

    public UserInfoVO getUserInfo(String userId) {
        TUser tUser = tUserRepository.getById(userId);
        return UserConvert.INSTANCE.parseToInfoVO(tUser);
    }

    public TUser getUserByAccount(String account) {
        return tUserRepository.lambdaQuery().eq(TUser::getAccount, account).one();
    }

    public PageVO<List<UserListVO>> list(UserListDTO userListDTO) {
        Page<TUser> page = new Page<>(userListDTO.getCurrent(), userListDTO.getPageSize());
        tUserRepository.lambdaQuery()
                .like(StrUtil.isNotBlank(userListDTO.getNickname()),TUser::getNickname,userListDTO.getNickname())
                .orderByDesc(BaseEntity::getCreateTime)
                .page(page);
        List<TUser> records = page.getRecords();
        List<UserListVO> list = UserConvert.INSTANCE.parseToListVO(records);
        return new PageVO<>(userListDTO.getCurrent(), userListDTO.getPageSize(), page.getTotal(), list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(UserAddDTO userAddDTO) {
        TUser tUser = UserConvert.INSTANCE.parseAddToEntity(userAddDTO);
        // 加密
        tUser.setPassword(BCrypt.hashpw(userAddDTO.getPassword()));
        tUserRepository.save(tUser);

        // 分配角色
        roleService.distributionRole(tUser.getId(), userAddDTO.getRoleIds());
    }

    public void update(UserUpdateDTO userUpdateDTO) {
        TUser tUser = UserConvert.INSTANCE.parseUpdateToEntity(userUpdateDTO);
        tUserRepository.updateById(tUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        // 删除用户
        tUserRepository.removeById(id);
        // 删除用户角色关联
        roleService.deleteRoleByUserId(id);
    }

    public UserDetailVO detail(String id) {
        TUser tUser = tUserRepository.getById(id);
        if (ObjUtil.isEmpty(tUser)){
            throw new SaTokenException(400,"用户不存在");
        }

        // 获取角色
        List<String> roleIds = roleService.getRoleList(id).stream().map(RoleVO::getId).toList();

        return UserConvert.INSTANCE.parseToDetailVO(tUser,roleIds);
    }

    public void updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO) {
        TUser tUser = tUserRepository.getById(userUpdatePasswordDTO.getId());
        if (ObjUtil.isEmpty(tUser)){
            throw new SaTokenException(400,"用户不存在");
        }
        // 验证原密码
        if (!BCrypt.checkpw(userUpdatePasswordDTO.getOldPassword(), tUser.getPassword())){
            throw new SaTokenException(400,"原密码错误");
        }
        // 校验两次密码是否一样
        if (userUpdatePasswordDTO.getOldPassword().equals(userUpdatePasswordDTO.getNewPassword())){
            throw new SaTokenException(400,"旧密码和新密码不能一样");
        }
        // 加密
        tUser.setPassword(BCrypt.hashpw(userUpdatePasswordDTO.getNewPassword()));
        tUserRepository.updateById(tUser);
    }

    public void distributionRole(UserDistributionRoleDTO userDistributionRoleDTO) {
        roleService.distributionRole(userDistributionRoleDTO.getUserId(), userDistributionRoleDTO.getRoleIds());
    }
}
