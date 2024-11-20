package com.baotruongtuan.RdpServer.config;

import java.util.Arrays;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.baotruongtuan.RdpServer.entity.Role;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.enums.UserRole;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.repository.RoleRepository;
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
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            // Initialize roles first
            if (!roleRepository.existsById(UserRole.ADMIN.getId())
                    || !roleRepository.existsById(UserRole.STAFF.getId())) {

                Role adminRole = Role.builder()
                        .id(UserRole.ADMIN.getId())
                        .name(UserRole.ADMIN.getName())
                        .build();

                Role staffRole = Role.builder()
                        .id(UserRole.STAFF.getId())
                        .name(UserRole.STAFF.getName())
                        .build();

                roleRepository.saveAll(Arrays.asList(adminRole, staffRole));
                log.info("Roles ADMIN and STAFF have been initialized.");
            }

            // Initialize admin user
            if (userRepository.findByUsername("admin@gmail.com") == null) {
                Role adminRole = roleRepository
                        .findById(UserRole.ADMIN.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION));

                User adminUser = User.builder()
                        .role(adminRole)
                        .name("admin")
                        .username("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .build();

                userRepository.save(adminUser);
                log.warn("Admin user created with default password: admin. Please change it!");
            }
        };
    }
}
