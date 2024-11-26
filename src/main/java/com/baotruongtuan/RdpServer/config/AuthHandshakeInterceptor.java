package com.baotruongtuan.RdpServer.config;

import com.baotruongtuan.RdpServer.utils.JwtUtilHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    JwtUtilHelper jwtUtilHelper;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        List<String> authHeaders = request.getHeaders().get("Authorization");

        if (authHeaders != null && !authHeaders.isEmpty()) {
            String token = authHeaders.get(0).replace("Bearer ", "");

            var verifiedJWT = jwtUtilHelper.verifyToken(token);
            String username = verifiedJWT.getJWTClaimsSet().getSubject();
            attributes.put("username", username);
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
    }
}
