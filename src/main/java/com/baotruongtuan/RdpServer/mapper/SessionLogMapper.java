package com.baotruongtuan.RdpServer.mapper;

import com.baotruongtuan.RdpServer.dto.SessionLogDTO;
import com.baotruongtuan.RdpServer.entity.SessionLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SessionMessageMapper.class)
public interface SessionLogMapper {
    SessionLogDTO toSessionLogDTO(SessionLog sessionLog);
}
