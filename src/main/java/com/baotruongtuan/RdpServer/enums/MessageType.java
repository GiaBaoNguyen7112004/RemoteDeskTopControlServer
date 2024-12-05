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
    STOP_SHARE_SCREEN("stop-share-screen"),
    DISCONNECT("disconnect"),
    NOTIFY_SESSION("notify-session");

    String name;
}
