package com.baotruongtuan.RdpServer.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum ErrorCode {
    NO_DATA_EXCEPTION(201, "No data was found", HttpStatus.NOT_FOUND),
    UNCATEGORIED_EXCEPTION(999, "Uncategoried exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(202, "Unauthenticated", HttpStatus.UNAUTHORIZED);

    int code;
    String message;
    HttpStatus httpStatus;
}
