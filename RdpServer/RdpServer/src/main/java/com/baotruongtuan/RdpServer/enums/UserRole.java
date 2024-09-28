package com.baotruongtuan.RdpServer.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum UserRole {
    ADMIN(1, "ADMIN"),
    STAFF(2, "STAFF");

    int id;
    String name;
}
