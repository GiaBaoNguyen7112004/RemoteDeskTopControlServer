package com.baotruongtuan.RdpServer.service.imp;

import com.baotruongtuan.RdpServer.dto.UserDTO;
import com.baotruongtuan.RdpServer.payload.request.UserCreationRequest;
import com.baotruongtuan.RdpServer.payload.request.UserUpdateRequest;

import java.util.List;

public interface UserServiceImp {
    public UserDTO createUser(UserCreationRequest userCreationRequest);
    public List<UserDTO> getAllUsers();
    public boolean deleteUser(int id);
    public UserDTO getUser(int id);
    public UserDTO  updateUser(UserUpdateRequest userUpdateRequest);
}
