package com.baotruongtuan.RdpServer.handler;

import com.baotruongtuan.RdpServer.dto.ClientMessageDTO;
import com.baotruongtuan.RdpServer.entity.SessionLog;
import com.baotruongtuan.RdpServer.entity.SessionMessage;
import com.baotruongtuan.RdpServer.enums.MessageStatus;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.repository.SessionLogRepository;
import com.baotruongtuan.RdpServer.repository.SessionMessageRepository;
import com.baotruongtuan.RdpServer.repository.UserRepository;
import com.baotruongtuan.RdpServer.utils.FeedbackMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppSocketHandler implements WebSocketHandler {
    UserRepository userRepository;
    SessionLogRepository sessionLogRepository;
    SessionMessageRepository sessionMessageRepository;
    Map<String, SessionLog> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = session.getAttributes().get("username").toString();

        SessionLog sessionLog = SessionLog.builder()
                .id(session.getId())
                .startTime(LocalDateTime.now())
                .user(userRepository.findByUsername(username).orElseThrow(()
                        -> new AppException(ErrorCode.NO_DATA_EXCEPTION)))
                .build();

        SessionMessage sessionMessage = SessionMessage.builder()
                .time(LocalDateTime.now())
                .content(FeedbackMessage.CONNECT_SUCCESS)
                .title(MessageStatus.INFO.name())
                .sessionLog(sessionLog)
                .build();
        sessionMessageRepository.save(sessionMessage);

        activeSessions.put(username, sessionLog);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        ClientMessageDTO clientMessageDTO = mapper.readValue(payload, ClientMessageDTO.class);

        Integer clientId = clientMessageDTO.getClientId();
        SessionLog sessionLog = activeSessions.get(clientId);

        if (sessionLog != null) {
            SessionMessage sessionMessage = SessionMessage.builder()
                    .content(clientMessageDTO.getAction())
                    .time(LocalDateTime.now())
                    .sessionLog(sessionLog)
                    .build();

            sessionMessageRepository.save(sessionMessage);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session.getAttributes().get("username").toString();

        SessionLog sessionLog = activeSessions.remove(username);
        if (sessionLog != null) {
            sessionLog.setEndTime(LocalDateTime.now());
            sessionLogRepository.save(sessionLog);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

//    private String parseQueryParam(String query, String key) {
//        if (query == null) return null;
//        return Arrays.stream(query.split("&"))
//                .map(param -> param.split("="))
//                .filter(kv -> kv[0].equals(key))
//                .map(kv -> kv[1])
//                .findFirst()
//                .orElse(null);
//    }
}
