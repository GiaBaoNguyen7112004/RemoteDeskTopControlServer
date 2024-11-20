package com.baotruongtuan.RdpServer.mapper;

import com.baotruongtuan.RdpServer.dto.AvatarDTO;
import com.baotruongtuan.RdpServer.entity.Avatar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AvatarMapper {
    AvatarDTO toAvatarDTO(Avatar avatar);
}
