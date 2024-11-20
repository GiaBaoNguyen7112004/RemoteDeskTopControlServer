package com.baotruongtuan.RdpServer.dto;

import java.util.List;

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
    String email;
    String jobTitle;
    byte[] avatar;
    String companyName;
    RoleDTO roleDTO;
    List<DepartmentDTO> departmentDTOs;
}
