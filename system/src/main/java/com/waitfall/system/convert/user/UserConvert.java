package com.waitfall.system.convert.user;

import com.waitfall.system.domain.dto.user.UserAddDTO;
import com.waitfall.system.domain.dto.user.UserUpdateDTO;
import com.waitfall.system.domain.entity.TUser;
import com.waitfall.system.domain.vo.user.UserDetailVO;
import com.waitfall.system.domain.vo.user.UserInfoVO;
import com.waitfall.system.domain.vo.user.UserListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author by 秋
 * @date 2025/7/14 18:24
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    List<UserListVO> parseToListVO(List<TUser> records);

    TUser parseAddToEntity(UserAddDTO userAddDTO);

    TUser parseUpdateToEntity(UserUpdateDTO userUpdateDTO);

    UserDetailVO parseToDetailVO(TUser tUser, List<String> roleIds);

    UserInfoVO parseToInfoVO(TUser tUser);
}
