package com.baotruongtuan.RdpServer.dto;

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
public class UserDTO {
    int id;
    String name;
    String password;
    String username;
    LocalDate dob;
    String email;
    RoleDTO roleDTO;
}
