package com.baotruongtuan.RdpServer.handler;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.baotruongtuan.RdpServer.enums.MessageType;
import com.baotruongtuan.RdpServer.enums.UserRole;
import com.baotruongtuan.RdpServer.repository.AccessRestrictionsRepository;

import com.baotruongtuan.RdpServer.utils.DomainExtractHelper;
import com.baotruongtuan.RdpServer.utils.SessionAttribute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.*;

import com.baotruongtuan.RdpServer.dto.SessionEventDTO;
import com.baotruongtuan.RdpServer.entity.SessionEvent;
import com.baotruongtuan.RdpServer.entity.SessionLog;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.mapper.SessionEventMapper;
import com.baotruongtuan.RdpServer.message.SocketMessage;
import com.baotruongtuan.RdpServer.repository.SessionEventRepository;
import com.baotruongtuan.RdpServer.repository.SessionLogRepository;
import com.baotruongtuan.RdpServer.repository.UserRepository;

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
    DomainExtractHelper domainExtractHelper;
    AccessRestrictionsRepository accessRestrictionsRepository;
    Set<String> violations = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {}

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        SocketMessage clientSocketMessage = socketMessageMapper(message);
        log.info("Nguoi dung gui 1 tin nhan voi type: {}", convertJsonToText(message, "type"));
        log.info("Nguoi dung gui data: {}", getData(message));
        switch (convertJsonToText(message, "type")) {
            case "authentication": {
                handleAuthentication(session, message);
                break;
            }
            case "start-share-screen": {
                handleStartShareScreen(session, message);
                break;
            }

            case "offer": {
                handleOffer(session, clientSocketMessage);
                break;
            }

            case "answer": {
                handleAnswer(session, clientSocketMessage);
                break;
            }

            case "ice-candidate": {
                handleIceCandidate(session, clientSocketMessage);
                break;
            }

            case "active-app": {
                handleActiveApp(session, message);
                break;
            }
            default:
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
                Integer.parseInt(session.getAttributes().get(SessionAttribute.CLIENT_ID).toString());

        WebSocketSession removeSession = activeSessions.remove(clientId);

        if (removeSession == null) {
            log.warn("Session not found in activeSessions. ClientId: {}", clientId);
        } else {
            SessionLog sessionLog = getSessionLog(removeSession);
            SessionEvent sessionEvent = SessionEvent.builder()
                    .time(LocalDateTime.now())
                    .content(FeedbackMessage.DISCONNECT_SUCCESS)
                    .title(MessageType.INFO.getName())
                    .build();

            sessionLog.setEndTime(LocalDateTime.now());
            sessionLog.getSessionEvents().add(sessionEvent);
            SessionLog theSessionLog = sessionLogRepository.save(sessionLog);

            sessionLog.getSessionEvents()
                    .forEach(theSessionEvent -> theSessionEvent.setSessionLog(theSessionLog));
            sessionEventRepository.saveAll(sessionLog.getSessionEvents());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void establishConnection(WebSocketSession session) throws Exception {
        Integer clientId =
                Integer.parseInt(session.getAttributes().get(SessionAttribute.CLIENT_ID).toString());

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
                .title(MessageType.INFO.getName())
                .build();
        sessionLog.getSessionEvents().add(sessionEvent);

        SessionEventDTO sessionEventDTO = sessionEventMapper.toSessionEventDTO(sessionEvent);
        SocketMessage socketMessage =
                SocketMessage.builder().type(MessageType.NOTIFY.getName()).data(sessionEventDTO).build();

        session.getAttributes().put(SessionAttribute.SESSION_LOG, sessionLog);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(socketMessage)));
        activeSessions.put(clientId, session);

        log.info("New client with id:{} connected to server", clientId);
    }

    public SessionLog getSessionLog(WebSocketSession session) {
        return (SessionLog) session.getAttributes().get(SessionAttribute.SESSION_LOG);
    }
    public void addEventForSessionLog(SessionLog sessionLog, SessionEvent sessionEvent) {
        sessionLog.getSessionEvents().add(sessionEvent);
    }
    public String convertJsonToText(WebSocketMessage<?> message, String target){
        if (message instanceof TextMessage textMessage) {
            String payload = textMessage.getPayload();
            try{
                JsonNode jsonNode = mapper.readTree(payload);
                return target.equals("type") ? jsonNode.get("type").asText() :
                        jsonNode.path("data").path(target).asText();
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        else {
            log.info("Unsupported message type: {}", message.getClass().getName());
        }
        return null;
    }

    public SocketMessage socketMessageMapper(WebSocketMessage<?> message){
        if (message instanceof TextMessage textMessage) {
            String payload = textMessage.getPayload();
            try {
                return mapper.readValue(payload, SocketMessage.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        else {
            log.info("Cannot map payload to SocketMessage");
        }

        return null;
    }

    public void setAttributeForSession(WebSocketSession session, SignedJWT signedJWT, String key, String value){
        if(signedJWT != null){
            try {
                session.getAttributes().put(key, signedJWT
                        .getJWTClaimsSet()
                        .getClaim(value)
                        .toString());
            } catch (ParseException e) {
                log.error(e.getMessage());
            }
        }
        else session.getAttributes().put(key, value);
    }

    public Object getData(WebSocketMessage<?> message){
        if (message instanceof TextMessage textMessage) {
            String payload = textMessage.getPayload();
            try {
                JsonNode jsonNode = mapper.readTree(payload);
                return jsonNode.get("data");
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        else {
            log.info("Unsupported message type: {}", message.getClass().getName());
        }
        return null;
    }

    public void handleAuthentication(WebSocketSession session, WebSocketMessage<?> message){
        String token = convertJsonToText(message, "token");
        try {
            SignedJWT verifiedJWT = jwtUtilHelper.verifyToken(token);
            String username = verifiedJWT.getJWTClaimsSet().getSubject();

            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION));
            userRepository.save(user);

            session.getAttributes().put(SessionAttribute.USERNAME, username);
            setAttributeForSession(session, verifiedJWT, SessionAttribute.ROLE, "scope" );
            setAttributeForSession(session, verifiedJWT, SessionAttribute.CLIENT_ID, "id");

            establishConnection(session);
        } catch (Exception e) {
            SessionEventDTO sessionEventDTO = SessionEventDTO.builder()
                    .title(MessageType.INFO.getName())
                    .time(LocalDateTime.now())
                    .content(ErrorCode.UNAUTHENTICATED.getMessage())
                    .build();
            try{
                session.sendMessage(new TextMessage(mapper.writeValueAsString(sessionEventDTO)));
                session.close();
            } catch (IOException ioeException) {
                log.error(ioeException.getMessage());
            }
        }
    }

    public void handleStartShareScreen(WebSocketSession session, WebSocketMessage<?> message){
        int departmentId;
        try {
            departmentId = Integer.parseInt(convertJsonToText(message, "departmentId"));
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_DATA);
        }

        List<User> users = userRepository.findByDepartmentId(departmentId);
        Set<Integer> setId = users.stream().map(User::getId).collect(Collectors.toSet());

        List<WebSocketSession> staffSessions = activeSessions.values().stream()
                .filter(staffSession -> {
                    if (staffSession.equals(session)) {
                        return false;
                    }
                    Object clientId = staffSession.getAttributes().get(SessionAttribute.CLIENT_ID);
                    int value = (clientId instanceof Integer)
                            ? (int) clientId
                            : Integer.parseInt(clientId.toString());
                    return setId.contains(value);
                })
                .toList();


        SocketMessage staffSocketMessage =
                SocketMessage.builder().type(MessageType.START_SHARE_SCREEN.getName()).build();

        staffSessions.forEach(staffSession -> {
            try {
                staffSession.sendMessage(new TextMessage(mapper.writeValueAsString(staffSocketMessage)));
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        });
    }

    public void handleOffer(WebSocketSession session, SocketMessage clientSocketMessage)
    {
        Integer clientId =
                Integer.parseInt(session.getAttributes().get(SessionAttribute.CLIENT_ID).toString());
        List<WebSocketSession> adminSessions = activeSessions.values().stream()
                .filter(activeSession -> {
                    if (activeSession.equals(session)) {
                        return false;
                    }
                    return activeSession.getAttributes()
                            .get(SessionAttribute.ROLE).equals("ROLE_" + UserRole.ADMIN.getName());
                })
                .toList();
        SocketMessage adminSocketMessage = SocketMessage.builder()
                .type(MessageType.OFFER.getName())
                .data(clientSocketMessage.getData())
                .clientId(clientId)
                .build();
        adminSessions.forEach(adminSession -> {
            try {
                adminSession.sendMessage(new TextMessage(mapper.writeValueAsString(adminSocketMessage)));
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        });
    }

    public void handleAnswer(WebSocketSession session, SocketMessage clientSocketMessage)
    {
        List<WebSocketSession> staffSessions = getStaffSessions(session, clientSocketMessage);

        SocketMessage staffSocketMessage = SocketMessage.builder()
                .type(MessageType.ANSWER.getName())
                .data(clientSocketMessage.getData())
                .build();

        staffSessions.forEach(staffSession -> {
            try {
                staffSession.sendMessage(new TextMessage(mapper.writeValueAsString(staffSocketMessage)));
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        });
    }

    public void handleIceCandidate(WebSocketSession session, SocketMessage clientSocketMessage)
    {
        List<WebSocketSession> staffSessions = getStaffSessions(session, clientSocketMessage);

        SocketMessage staffSocketMessage = SocketMessage.builder()
                .type(MessageType.ICE_CANDIDATE.getName())
                .data(clientSocketMessage.getData())
                .build();

        staffSessions.forEach(staffSession -> {
            try {
                staffSession.sendMessage(new TextMessage(mapper.writeValueAsString(staffSocketMessage)));
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        });
    }

    public void handleActiveApp(WebSocketSession session, WebSocketMessage<?> message)
    {
        try{
            String content = convertJsonToText(message, "content");
            String processedContent;

            if(domainExtractHelper.isValidUrl(content))
            {
                processedContent = domainExtractHelper.extractDomain(content);
            }
            else processedContent = content;

            if(!violations.contains(processedContent) &&
                    accessRestrictionsRepository.existsAccessRestrictionByContent(processedContent))
            {
                SessionEvent sessionEvent = SessionEvent.builder()
                        .title(MessageType.ERROR.getName())
                        .time(LocalDateTime.now())
                        .author("admin")
                        .content("You are not permitted to access  " + processedContent)
                        .build();
                addEventForSessionLog(getSessionLog(session), sessionEvent);
                violations.add(processedContent);
                SessionEventDTO sessionEventDTO = sessionEventMapper.toSessionEventDTO(sessionEvent);
                SocketMessage socketMessage =
                        SocketMessage.builder().type(MessageType.ERROR.getName()).data(sessionEventDTO).build();

                session.sendMessage(new TextMessage(mapper.writeValueAsString(socketMessage)));
            }
        } catch (Exception e) {log.info(e.getMessage());}
    }

    public List<WebSocketSession> getStaffSessions(WebSocketSession session, SocketMessage clientSocketMessage) {
        Integer clientId = clientSocketMessage.getClientId();
        return activeSessions.values().stream()
                .filter(staffSession -> {
                    if (staffSession.equals(session)) {
                        return false;
                    }
                    return Integer
                            .parseInt(session.getAttributes().get(SessionAttribute.CLIENT_ID).toString()) == clientId;
                })
                .toList();
    }
}
