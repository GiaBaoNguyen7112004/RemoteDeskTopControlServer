package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.baotruongtuan.RdpServer.dto.UserDTO;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.payload.request.UserCreationRequest;
import com.baotruongtuan.RdpServer.payload.request.UserUpdatingRequest;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class, DepartmentMapper.class})
public interface UserMapper {
    final DepartmentMapper departmentMapper = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "password", ignore = true)
    User toUser(UserCreationRequest userCreationRequest);

    @Mapping(target = "roleDTO", source = "role")
    UserDTO toUserDTO(User user);

    @AfterMapping
    default void afterMapping(@MappingTarget UserDTO.UserDTOBuilder userDTO, User user) {

        if (user.getDepartmentDetails() != null) {
            userDTO.departmentDTOs(user.getDepartmentDetails().stream()
                    .map(departmentDetail -> departmentMapper.toDepartmentDTO(departmentDetail.getDepartment()))
                    .toList());
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdatingRequest userUpdatingRequest);
}
