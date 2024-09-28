package com.baotruongtuan.RdpServer.service;

import com.baotruongtuan.RdpServer.dto.RoleDTO;
import com.baotruongtuan.RdpServer.dto.UserDTO;
import com.baotruongtuan.RdpServer.entity.Role;
import com.baotruongtuan.RdpServer.entity.User;
import com.baotruongtuan.RdpServer.enums.UserRole;
import com.baotruongtuan.RdpServer.exception.AppException;
import com.baotruongtuan.RdpServer.exception.ErrorCode;
import com.baotruongtuan.RdpServer.mapper.RoleMapper;
import com.baotruongtuan.RdpServer.mapper.UserMapper;
import com.baotruongtuan.RdpServer.payload.request.UserCreationRequest;
import com.baotruongtuan.RdpServer.payload.request.UserUpdateRequest;
import com.baotruongtuan.RdpServer.repository.UserRepository;
import com.baotruongtuan.RdpServer.service.imp.UserServiceImp;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService implements UserServiceImp {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleMapper roleMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserCreationRequest userCreationRequest) {
        int roleID = userCreationRequest.getRoleID();
        Role role = Role.builder()
                .id(roleID)
                .name(
                        roleID == UserRole.ADMIN.getId()
                        ? UserRole.ADMIN.getName() : UserRole.STAFF.getName()
                )
                .build();

        User user = userMapper.toUser(userCreationRequest);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        userRepository.save(user);

        return userMapper.toUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            RoleDTO roleDTO = roleMapper.toRoleDTO(user.getRole());
            UserDTO userDTO = userMapper.toUserDTO(user);
            userDTO.setRoleDTO(roleDTO);

            return userDTO;
        })
                .toList();
    }

    @Override
    public boolean deleteUser(int id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION)));
        return true;
    }

    @Override
    public UserDTO getUser(int id) {
        return userMapper.toUserDTO(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION)));
    }

    @Override
    public UserDTO updateUser(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXCEPTION));
        userMapper.updateUser(user, userUpdateRequest);

        userRepository.save(user);

        return userMapper.toUserDTO(user);
    }
}
