package com.baotruongtuan.RdpServer.service.imp;

import com.baotruongtuan.RdpServer.dto.AuthenticationDTO;
import com.baotruongtuan.RdpServer.payload.request.AuthenticationRequest;

public interface AuthenticationServiceImp {
    public AuthenticationDTO authenticate(AuthenticationRequest authenticationRequest);
}
