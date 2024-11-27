package com.baotruongtuan.RdpServer.handler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baotruongtuan.RdpServer.mapper.SessionMessageMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.*;

import com.baotruongtuan.RdpServer.entity.SessionLog;
import com.baotruongtuan.RdpServer.entity.SessionMessage;
import com.baotruongtuan.RdpServer.enums.MessageStatus;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.repository.SessionLogRepository;
import com.baotruongtuan.RdpServer.repository.SessionMessageRepository;
import com.baotruongtuan.RdpServer.repository.UserRepository;
import com.baotruongtuan.RdpServer.utils.FeedbackMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppSocketHandler implements WebSocketHandler {
    UserRepository userRepository;
    SessionLogRepository sessionLogRepository;
    SessionMessageRepository sessionMessageRepository;
    Map<String, SessionLog> activeSessions = new ConcurrentHashMap<>();
    List<SessionMessage> sessionMessages = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    SessionMessageMapper sessionMessageMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = session.getAttributes().get("username").toString();
        SessionLog sessionLog = SessionLog.builder()
                .id(session.getId())
                .startTime(LocalDateTime.now())
                .user(userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION)))
                .build();

        SessionMessage sessionMessage = SessionMessage.builder()
                .time(LocalDateTime.now())
                .content(FeedbackMessage.CONNECT_SUCCESS)
                .title(MessageStatus.INFO.name())
                .build();
        sessionMessages.add(sessionMessage);

        session.sendMessage
                (new TextMessage(mapper.writeValueAsString(sessionMessageMapper.toSessionMessageDTO(sessionMessage))));

        activeSessions.put(username, sessionLog);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        //        String payload = (String) message.getPayload();
        //        ObjectMapper mapper = new ObjectMapper();
        //        ClientMessageDTO clientMessageDTO = mapper.readValue(payload, ClientMessageDTO.class);
        //
        //        Integer clientId = clientMessageDTO.getClientId();
        //        SessionLog sessionLog = activeSessions.get(clientId);
        //
        //        if (sessionLog != null) {
        //            SessionMessage sessionMessage = SessionMessage.builder()
        //                    .content(clientMessageDTO.getAction())
        //                    .time(LocalDateTime.now())
        //                    .sessionLog(sessionLog)
        //                    .build();
        //
        //            sessionMessageRepository.save(sessionMessage);
        //        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Transactional
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session.getAttributes().get("username").toString();
        SessionLog removeSession = activeSessions.remove(username);

        if (removeSession != null) {
            removeSession.setEndTime(LocalDateTime.now());
            SessionLog sessionLog = sessionLogRepository.save(removeSession);

            SessionMessage sessionMessage = SessionMessage.builder()
                    .time(LocalDateTime.now())
                    .content(FeedbackMessage.DISCONNECT_SUCCESS)
                    .title(MessageStatus.INFO.name())
                    .build();

            sessionMessages.add(sessionMessage);
            sessionMessages.forEach(message -> message.setSessionLog(sessionLog));

            sessionMessageRepository.saveAll(sessionMessages);
        }
        else throw new AppException(ErrorCode.NO_DATA_EXCEPTION);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
