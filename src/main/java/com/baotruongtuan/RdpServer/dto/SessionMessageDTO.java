package com.baotruongtuan.RdpServer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionMessageDTO {
    String id;
    LocalDateTime time;
    String content;
}
