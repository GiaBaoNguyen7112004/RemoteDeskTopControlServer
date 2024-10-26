package com.baotruongtuan.RdpServer.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.baotruongtuan.RdpServer.entity.Role;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.enums.UserRole;
import com.baotruongtuan.RdpServer.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@Slf4j
public class AppInitConfig {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if (userRepository.findByUsername("admin") == null) {
                Role role = Role.builder().id(UserRole.ADMIN.getId()).build();

                User users = User.builder()
                        .role(role)
                        .name("admin")
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();

                log.warn("admin was created with default password: admin, please change it!");
                userRepository.save(users);
            }
        };
    }
}
