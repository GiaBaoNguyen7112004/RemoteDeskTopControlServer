package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.Mapper;

import com.baotruongtuan.RdpServer.dto.RoleDTO;
import com.baotruongtuan.RdpServer.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);
}
