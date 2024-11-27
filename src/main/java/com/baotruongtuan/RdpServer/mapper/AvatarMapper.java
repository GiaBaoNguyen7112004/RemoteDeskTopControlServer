package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.Mapper;

import com.baotruongtuan.RdpServer.dto.AvatarDTO;
import com.baotruongtuan.RdpServer.entity.Avatar;

@Mapper(componentModel = "spring")
public interface AvatarMapper {
    AvatarDTO toAvatarDTO(Avatar avatar);
}
