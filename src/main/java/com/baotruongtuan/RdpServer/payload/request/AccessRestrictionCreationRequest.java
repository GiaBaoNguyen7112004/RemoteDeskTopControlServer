package com.baotruongtuan.RdpServer.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccessRestrictionCreationRequest {
    String domain;
    String app;
}
