package com.baotruongtuan.RdpServer.service.imp;

import java.util.List;

import com.baotruongtuan.RdpServer.dto.SessionLogDTO;

public interface ISessionLogsService {
    List<SessionLogDTO> getUserSessionLogs(int userId);
}
