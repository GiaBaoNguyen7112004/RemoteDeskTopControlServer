package com.baotruongtuan.RdpServer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientMessageDTO {
    Integer clientId;
    String action;
}
