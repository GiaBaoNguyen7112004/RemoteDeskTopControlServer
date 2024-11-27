package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.Mapper;

import com.baotruongtuan.RdpServer.dto.SessionLogDTO;
import com.baotruongtuan.RdpServer.entity.SessionLog;

@Mapper(componentModel = "spring", uses = SessionMessageMapper.class)
public interface SessionLogMapper {
    SessionLogDTO toSessionLogDTO(SessionLog sessionLog);
}
