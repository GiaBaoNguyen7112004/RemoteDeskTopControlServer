package com.baotruongtuan.RdpServer.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum ErrorCode {
    NO_DATA_EXCEPTION(201, "No data was found", HttpStatus.NOT_FOUND),
    UNCATEGORIED_EXCEPTION(999, "Uncategoried exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(202, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(203, "Unauthorized", HttpStatus.FORBIDDEN),
    INVALID_USERNAME(204, "Username must be email", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(
            205,
            "Password must be at least {min} character and do not" + "exceed {max} characters",
            HttpStatus.BAD_REQUEST),
    INVALID_KEY(206, "Invalid key", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(207, "Invalid email", HttpStatus.BAD_REQUEST),
    NOT_EMPTY(208, "The information was not allowed to be empty", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;
}
