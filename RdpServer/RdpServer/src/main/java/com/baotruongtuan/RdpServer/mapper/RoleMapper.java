package com.baotruongtuan.RdpServer.mapper;

import com.baotruongtuan.RdpServer.dto.RoleDTO;
import com.baotruongtuan.RdpServer.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);
}
