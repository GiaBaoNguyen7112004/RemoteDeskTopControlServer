package com.baotruongtuan.RdpServer.mapper;

import com.baotruongtuan.RdpServer.dto.SessionMessageDTO;
import com.baotruongtuan.RdpServer.entity.SessionMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMessageMapper {
    SessionMessageDTO toSessionMessageDTO(SessionMessage sessionMessage);
}
