package com.baotruongtuan.RdpServer.service;

import com.baotruongtuan.RdpServer.dto.AuthenticationDTO;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.payload.request.AuthenticationRequest;
import com.baotruongtuan.RdpServer.repository.UserRepository;
import com.baotruongtuan.RdpServer.service.imp.AuthenticationServiceImp;
import com.baotruongtuan.RdpServer.utils.JwtUtilHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService implements AuthenticationServiceImp {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtilHelper jwtUtilHelper;

    @Override
    public AuthenticationDTO authenticate(AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        User user = userRepository.findByUsername(username);
        if(user == null || !passwordEncoder.matches(password, user.getPassword()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return AuthenticationDTO.builder()
                .authenticated(true)
                .token(jwtUtilHelper.generateToken(user))
                .build();
    }
}
