package com.baotruongtuan.RdpServer.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 6, max = 20, message = "INVALID_PASSWORD")
    String password;

    @Email(message = "INVALID_EMAIL")
    String username;
}
