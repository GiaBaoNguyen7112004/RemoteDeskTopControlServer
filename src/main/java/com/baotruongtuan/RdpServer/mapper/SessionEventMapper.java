package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.Mapper;

import com.baotruongtuan.RdpServer.dto.SessionEventDTO;
import com.baotruongtuan.RdpServer.entity.SessionEvent;

@Mapper(componentModel = "spring")
public interface SessionEventMapper {
    SessionEventDTO toSessionEventDTO(SessionEvent sessionEvent);
}
