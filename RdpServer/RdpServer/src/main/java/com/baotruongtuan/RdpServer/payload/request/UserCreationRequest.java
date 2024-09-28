package com.baotruongtuan.RdpServer.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String name;
    String password;
    String username;
    LocalDate dob;
    String email;
    int roleID;
}
