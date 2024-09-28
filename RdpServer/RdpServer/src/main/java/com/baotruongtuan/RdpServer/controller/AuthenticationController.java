package com.baotruongtuan.RdpServer.controller;

import com.baotruongtuan.RdpServer.payload.ResponseData;
import com.baotruongtuan.RdpServer.payload.request.AuthenticationRequest;
import com.baotruongtuan.RdpServer.payload.request.UserCreationRequest;
import com.baotruongtuan.RdpServer.service.imp.AuthenticationServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationServiceImp authenticationServiceImp;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest)
    {
        ResponseData responseData = ResponseData.builder()
                .data(authenticationServiceImp.authenticate(authenticationRequest))
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
