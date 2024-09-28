package com.baotruongtuan.RdpServer.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @NotEmpty(message = "NOT_EMPTY")
    String name;

    @Size(min = 6, max = 20, message = "INVALID_PASSWORD")
    String password;

    String username;
    LocalDate dob;

    @Email(message = "INVALID_EMAIL")
    String email;
    int roleID;
}
