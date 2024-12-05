package com.baotruongtuan.RdpServer.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.baotruongtuan.RdpServer.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.*;

import com.baotruongtuan.RdpServer.dto.SessionEventDTO;
import com.baotruongtuan.RdpServer.entity.SessionEvent;
import com.baotruongtuan.RdpServer.entity.SessionLog;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.enums.EventStatus;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.mapper.SessionEventMapper;
import com.baotruongtuan.RdpServer.message.SocketMessage;
import com.baotruongtuan.RdpServer.repository.SessionEventRepository;
import com.baotruongtuan.RdpServer.repository.SessionLogRepository;
import com.baotruongtuan.RdpServer.repository.UserRepository;
import com.baotruongtuan.RdpServer.service.imp.IDepartmentService;
import com.baotruongtuan.RdpServer.utils.FeedbackMessage;
import com.baotruongtuan.RdpServer.utils.JwtUtilHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    SessionEventRepository sessionEventRepository;
    SessionEventMapper sessionEventMapper;
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    Map<Integer, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    JwtUtilHelper jwtUtilHelper;
    IDepartmentService iDepartmentService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {}

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            String payload = ((TextMessage) message).getPayload();
            JsonNode jsonNode = mapper.readTree(payload);

            log.info(jsonNode.get("type").asText());
            log.info(mapper.writeValueAsString(jsonNode.get("data")));

            switch (jsonNode.get("type").asText()) {
                case "authentication": {
                    String token = jsonNode.path("data").path("token").asText();
                    try {
                        var verifiedJWT = jwtUtilHelper.verifyToken(token);
                        String username = verifiedJWT.getJWTClaimsSet().getSubject();

                        User user = userRepository
                                .findByUsername(username)
                                .orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION));
                        user.setIsOnline(true);
                        userRepository.save(user);

                        session.getAttributes().put("username", username);
                        session.getAttributes()
                                .put(
                                        "role",
                                        verifiedJWT
                                                .getJWTClaimsSet()
                                                .getClaim("scope")
                                                .toString());
                        session.getAttributes()
                                .put(
                                        "clientId",
                                        verifiedJWT
                                                .getJWTClaimsSet()
                                                .getClaim("id")
                                                .toString());
                        establishConnection(session);

//                        log.info(mapper.writeValueAsString(iDepartmentService.getMembersInDepartment(4)));
                    } catch (AppException e) {
                        SessionEventDTO sessionEventDTO = SessionEventDTO.builder()
                                .title(EventStatus.INFO.name())
                                .time(LocalDateTime.now())
                                .content(ErrorCode.UNAUTHENTICATED.getMessage())
                                .build();
                        session.sendMessage(new TextMessage(mapper.writeValueAsString(sessionEventDTO)));
                        session.close();
                    }
                    break;
                }
                case "start-share-screen": {

                    session.getAttributes().put("isSharing", 1);
                    List<WebSocketSession> staffSessions = activeSessions.values().stream()
                            .filter(staffSession ->
                                    staffSession.getAttributes().get("role").equals("ROLE_STAFF"))
                            .toList();
                    staffSessions.forEach(
                            staffSession -> staffSession.getAttributes().put("isSharing", 1));

                    SocketMessage staffSocketMessage =
                            SocketMessage.builder().type("start-share-screen").build();

                    staffSessions.forEach(staffSession -> {
                        try {
                            staffSession.sendMessage(new TextMessage(mapper.writeValueAsString(staffSocketMessage)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }

                case "offer": {
                    SocketMessage staffSocketMessage = mapper.readValue(payload, SocketMessage.class);
                    List<WebSocketSession> adminSessions = activeSessions.values().stream()
                            .filter(activeSession -> {
                                Object isSharing = activeSession.getAttributes().get("isSharing");
                                int value = (isSharing instanceof Integer)
                                        ? (int) isSharing
                                        : Integer.parseInt(isSharing.toString());
                                return value == 1;
                            })
                            .toList();

                    SocketMessage adminSocketMessage = SocketMessage.builder()
                            .type("offer")
                            .data(staffSocketMessage.getData())
                            .build();

                    adminSessions.forEach(adminSession -> {
                        try {
                            adminSession.sendMessage(new TextMessage(mapper.writeValueAsString(adminSocketMessage)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }

                case "answer": {
                    SocketMessage adminSocketMessage = mapper.readValue(payload, SocketMessage.class);
                    List<WebSocketSession> staffSessions = activeSessions.values().stream()
                            .filter(staffSession -> {
                                Object isSharing = staffSession.getAttributes().get("isSharing");
                                int value = (isSharing instanceof Integer)
                                        ? (int) isSharing
                                        : Integer.parseInt(isSharing.toString());

                                return value == 1;
                            })
                            .toList();

                    SocketMessage staffSocketMessage = SocketMessage.builder()
                            .type("answer")
                            .data(adminSocketMessage.getData())
                            .build();

                    staffSessions.forEach(staffSession -> {
                        try {
                            staffSession.sendMessage(new TextMessage(mapper.writeValueAsString(staffSocketMessage)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }

                case "ice-candidate": {
                    log.info("Co thang goi ice-candidate");
                    SocketMessage adminSocketMessage = mapper.readValue(payload, SocketMessage.class);
                    List<WebSocketSession> staffSessions = activeSessions.values().stream()
                            .filter(staffSession -> {
                                Object isSharing = staffSession.getAttributes().get("isSharing");
                                int value = (isSharing instanceof Integer)
                                        ? (int) isSharing
                                        : Integer.parseInt(isSharing.toString());

                                return value == 1;
                            })
                            .toList();

                    SocketMessage staffSocketMessage = SocketMessage.builder()
                            .type("ice-candidate")
                            .data(adminSocketMessage.getData())
                            .build();

                    staffSessions.forEach(staffSession -> {
                        try {
                            staffSession.sendMessage(new TextMessage(mapper.writeValueAsString(staffSocketMessage)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }

                    //                case "active-app" : {
                    //
                    //                }
            }
        } else {
            System.err.println("Unsupported message type: " + message.getClass().getName());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());
    }

    @Transactional
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Integer clientId =
                Integer.parseInt(session.getAttributes().get("clientId").toString());

        WebSocketSession removeSession = activeSessions.remove(clientId);
        if (removeSession == null) {
            log.warn("Session not found in activeSessions. ClientId: {}", clientId);
        } else {
            SessionLog sessionLog = (SessionLog) removeSession.getAttributes().get("sessionLog");
            SessionEvent sessionEvent = SessionEvent.builder()
                    .time(LocalDateTime.now())
                    .content(FeedbackMessage.DISCONNECT_SUCCESS)
                    .title(EventStatus.INFO.name())
                    .build();

            sessionLog.setEndTime(LocalDateTime.now());
            sessionLog.getSessionEvents().add(sessionEvent);
            SessionLog theSessionLog = sessionLogRepository.save(sessionLog);

            sessionLog.getSessionEvents().forEach(theSessionEvent -> theSessionEvent.setSessionLog(theSessionLog));
            sessionEventRepository.saveAll(sessionLog.getSessionEvents());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void establishConnection(WebSocketSession session) throws Exception {
        Integer clientId =
                Integer.parseInt(session.getAttributes().get("clientId").toString());

        SessionLog sessionLog = SessionLog.builder()
                .id(session.getId())
                .startTime(LocalDateTime.now())
                .user(userRepository
                        .findById(clientId)
                        .orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION)))
                .build();
        SessionEvent sessionEvent = SessionEvent.builder()
                .time(LocalDateTime.now())
                .content(FeedbackMessage.CONNECT_SUCCESS)
                .title(EventStatus.INFO.name())
                .build();
        sessionLog.getSessionEvents().add(sessionEvent);

        SessionEventDTO sessionEventDTO = sessionEventMapper.toSessionEventDTO(sessionEvent);
        SocketMessage socketMessage =
                SocketMessage.builder().type("notify").data(sessionEventDTO).build();

        session.getAttributes().put("sessionLog", sessionLog);
        log.info("thong bao tu sv:" + mapper.writeValueAsString(socketMessage));
        session.sendMessage(new TextMessage(mapper.writeValueAsString(socketMessage)));
        activeSessions.put(clientId, session);
        log.info("Da them session vao active sessions cho client co id" + clientId);
    }
}
