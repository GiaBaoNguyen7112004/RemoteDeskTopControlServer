package com.baotruongtuan.RdpServer.config;

import com.baotruongtuan.RdpServer.handler.AppSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketConfig implements WebSocketConfigurer {
    AppSocketHandler appSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(appSocketHandler, "/ws").setAllowedOrigins("*");
    }
}
