package com.baotruongtuan.RdpServer.exception;

import com.baotruongtuan.RdpServer.payload.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();

        ResponseData responseData = ResponseData.builder()
                .data(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        return new ResponseEntity<>(responseData, errorCode.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> appExceptionHandler(Exception e) {
        ErrorCode errorCode = ErrorCode.UNCATEGORIED_EXCEPTION;

        ResponseData responseData = ResponseData.builder()
                .data(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        return new ResponseEntity<>(responseData, errorCode.getHttpStatus());
    }
}
