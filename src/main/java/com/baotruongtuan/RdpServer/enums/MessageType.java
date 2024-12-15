package com.baotruongtuan.RdpServer.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MessageType {
    START_SHARE_SCREEN("start-share-screen"),
    OFFER("offer"),
    AUTHENTICATION("authentication"),
    ICE_CANDIDATE("ice-candidate"),
    ANSWER("answer"),
    STOP_SHARE_SCREEN("stop-share-screen"),
    DISCONNECT("disconnect"),
    NOTIFY("notify"),
    ERROR("error"),
    INFO("info"),
    ;
    String name;

    @Override
    public String toString() {
        return name;
    }
}
