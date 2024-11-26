package com.baotruongtuan.RdpServer.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionLogDTO {
    String id;
    LocalDateTime startTime;
    LocalDateTime endTime;
    List<SessionMessageDTO> sessionMessages;
}
