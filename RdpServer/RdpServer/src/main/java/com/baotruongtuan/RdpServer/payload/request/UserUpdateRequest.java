package com.baotruongtuan.RdpServer.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    int id;
    String name;
    String password;
    String email;
    LocalDate dob;
}
