package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.Mapper;

import com.baotruongtuan.RdpServer.dto.SessionMessageDTO;
import com.baotruongtuan.RdpServer.entity.SessionMessage;

@Mapper(componentModel = "spring")
public interface SessionMessageMapper {
    SessionMessageDTO toSessionMessageDTO(SessionMessage sessionMessage);
}
