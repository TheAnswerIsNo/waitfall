package com.waitfall.system.service;

import cn.hutool.core.collection.CollUtil;
import com.waitfall.framework.pojo.BaseService;
import com.waitfall.system.convert.role.RoleConvert;
import com.waitfall.system.domain.entity.TRole;
import com.waitfall.system.domain.entity.TUserRole;
import com.waitfall.system.domain.repository.TRoleRepository;
import com.waitfall.system.domain.repository.TUserRoleRepository;
import com.waitfall.system.domain.vo.role.RoleVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @date 2025/7/14 16:21
 * @author by 秋
 */
@Service
public class RoleService extends BaseService {

    @Resource
    private TRoleRepository tRoleRepository;

    @Resource
    private TUserRoleRepository tUserRoleRepository;

    public List<RoleVO> getRoleList(String userId){
        List<String> roleIds = tUserRoleRepository.lambdaQuery()
                .eq(TUserRole::getUserId, userId).list()
                .stream().map(TUserRole::getRoleId).toList();
        if (CollUtil.isEmpty(roleIds)){
            return List.of();
        }
        List<TRole> list = tRoleRepository.lambdaQuery()
                .in(TRole::getId, roleIds)
                .orderByDesc(TRole::getSort)
                .orderByDesc(TRole::getCreateTime)
                .list();
        return RoleConvert.INSTANCE.parseToListVO(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void distributionRole(String userId, List<String> roleIds) {
        tUserRoleRepository.lambdaUpdate().eq(TUserRole::getUserId, userId).remove();
        if (CollUtil.isNotEmpty(roleIds)){
            List<TUserRole> tUserRoleList = roleIds.stream().map(roleId -> TUserRole.builder().roleId(roleId).userId(userId).build()).toList();
            tUserRoleRepository.saveBatch(tUserRoleList, 1000);
        }
    }

    public void deleteRoleByUserId(String userId){
        tUserRoleRepository.lambdaUpdate().eq(TUserRole::getUserId, userId).remove();
    }

}
