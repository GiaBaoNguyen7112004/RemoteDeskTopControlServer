package com.baotruongtuan.RdpServer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baotruongtuan.RdpServer.payload.request.AuthenticationRequest;
import com.baotruongtuan.RdpServer.payload.request.IntrospectRequest;
import com.baotruongtuan.RdpServer.payload.request.LogOutRequest;
import com.baotruongtuan.RdpServer.payload.response.ResponseData;
import com.baotruongtuan.RdpServer.service.imp.AuthenticationServiceImp;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationServiceImp authenticationServiceImp;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        ResponseData responseData = ResponseData.builder()
                .data(authenticationServiceImp.authenticate(authenticationRequest))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/introspect")
    public ResponseEntity<?> introspect(@RequestBody IntrospectRequest introspectRequest) {
        ResponseData responseData = ResponseData.builder()
                .data(authenticationServiceImp.introspect(introspectRequest))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/logOut")
    public ResponseEntity<?> logOut(@RequestBody LogOutRequest logOutRequest) {
        ResponseData responseData = ResponseData.builder()
                .data(authenticationServiceImp.logOut(logOutRequest))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
