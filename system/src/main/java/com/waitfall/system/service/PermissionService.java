package com.waitfall.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.TreeUtil;
import com.waitfall.framework.pojo.BaseService;
import com.waitfall.system.domain.entity.TPermission;
import com.waitfall.system.domain.entity.TRolePermission;
import com.waitfall.system.domain.repository.TPermissionRepository;
import com.waitfall.system.domain.repository.TRolePermissionRepository;
import com.waitfall.system.domain.vo.permission.PermissionListVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @date 2025/7/14 16:22
 * @author by 秋
 */
@Service
public class PermissionService extends BaseService {

    @Resource
    private TPermissionRepository tPermissionRepository;

    @Resource
    private TRolePermissionRepository tRolePermissionRepository;

    public List<PermissionListVO> getPermissionList(List<String> roleIds){
        if (CollUtil.isEmpty(roleIds)){
            return List.of();
        }
        List<String> permissionIds = tRolePermissionRepository.lambdaQuery()
                .in(TRolePermission::getRoleId, roleIds).list()
                .stream().map(TRolePermission::getPermissionId).toList();
        if (CollUtil.isEmpty(permissionIds)){
            return List.of();
        }
        List<TPermission> list = tPermissionRepository.lambdaQuery()
                .in(TPermission::getId, permissionIds)
                .orderByDesc(TPermission::getSort)
                .orderByDesc(TPermission::getCreateTime)
                .list();

        List<PermissionListVO> permissionVOList = BeanUtil.copyToList(list, PermissionListVO.class);
        return TreeUtil.build(permissionVOList, "0", PermissionListVO::getId, PermissionListVO::getParentId, PermissionListVO::setChildren);
    }

}
