package com.waitfall.system.convert.role;

import com.waitfall.system.domain.entity.TRole;
import com.waitfall.system.domain.vo.role.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/15 11:49
 */
@Mapper
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    List<RoleVO> parseToListVO(List<TRole> roleList);
}
