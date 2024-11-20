package com.baotruongtuan.RdpServer.service.imp;

import java.util.List;

import com.baotruongtuan.RdpServer.dto.AvatarDTO;
import com.baotruongtuan.RdpServer.dto.DepartmentDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.baotruongtuan.RdpServer.dto.UserDTO;
import com.baotruongtuan.RdpServer.payload.request.UserCreationRequest;
import com.baotruongtuan.RdpServer.payload.request.UserUpdatingRequest;

public interface IUserService {
    UserDTO createUser(UserCreationRequest userCreationRequest);

    List<UserDTO> getAllUsers();

    void deleteUser(int id);

    UserDTO getUser(int id);

    UserDTO updateUser(int id, UserUpdatingRequest userUpdatingRequest);

    @Transactional
    DepartmentDTO joinDepartment(@PathVariable int userId, @PathVariable String departmentCode);

    @Transactional
    void leaveDepartment(@PathVariable int userId, @PathVariable int departmentId);
}
