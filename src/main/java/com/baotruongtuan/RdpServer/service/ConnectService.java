package com.baotruongtuan.RdpServer.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.baotruongtuan.RdpServer.dto.ConnectInfoDTO;
import com.baotruongtuan.RdpServer.service.imp.IConnectService;
import com.baotruongtuan.RdpServer.utils.FeedbackMessage;

@Service
public class ConnectService implements IConnectService {
    @Override
    public ConnectInfoDTO getConnectInfo(WebSocketSession session) {
        return ConnectInfoDTO.builder()
                .content("Connection established on session: " + session.getId())
                .time(new Date(Instant.now().toEpochMilli()))
                .title(FeedbackMessage.CONNECT_SUCCESS)
                .build();
    }

    @Override
    public ConnectInfoDTO getDisconnectInfo(WebSocketSession session, CloseStatus closeStatus) {
        return ConnectInfoDTO.builder()
                .content("Connection closed on session: " + session.getId() + " with status: " + closeStatus.getCode())
                .time(new Date(Instant.now().toEpochMilli()))
                .title(FeedbackMessage.DISCONNECT_SUCCESS)
                .build();
    }
}
