package com.baotruongtuan.RdpServer.mapper;

import com.baotruongtuan.RdpServer.dto.UserDTO;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.payload.request.UserCreationRequest;
import com.baotruongtuan.RdpServer.payload.request.UserUpdateRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toUser(UserCreationRequest userCreationRequest);

    @Mapping(target = "roleDTO", source = "role")
    UserDTO toUserDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}
