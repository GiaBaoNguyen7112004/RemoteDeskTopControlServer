package com.baotruongtuan.RdpServer.service.imp;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.baotruongtuan.RdpServer.dto.ConnectInfoDTO;

public interface IConnectService {

    ConnectInfoDTO getConnectInfo(WebSocketSession session);

    ConnectInfoDTO getDisconnectInfo(WebSocketSession session, CloseStatus closeStatus);
}
