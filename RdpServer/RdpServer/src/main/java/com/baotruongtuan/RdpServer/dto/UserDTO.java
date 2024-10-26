package com.baotruongtuan.RdpServer.dto;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
