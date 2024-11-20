package com.baotruongtuan.RdpServer.payload.request;

import jakarta.validation.constraints.Email;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdatingRequest {
    String name;
    String password;

    @Email(message = "INVALID_EMAIL")
    String email;

    String jobTitle;
    String companyName;
}
