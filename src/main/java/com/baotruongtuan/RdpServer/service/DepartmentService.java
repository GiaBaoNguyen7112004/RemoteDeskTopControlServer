package com.baotruongtuan.RdpServer.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.baotruongtuan.RdpServer.dto.DepartmentDTO;
import com.baotruongtuan.RdpServer.dto.UserDTO;
import com.baotruongtuan.RdpServer.entity.Department;
import com.baotruongtuan.RdpServer.mapper.DepartmentMapper;
import com.baotruongtuan.RdpServer.mapper.UserMapper;
import com.baotruongtuan.RdpServer.payload.request.DepartmentCreationRequest;
import com.baotruongtuan.RdpServer.repository.DepartmentDetailRepository;
import com.baotruongtuan.RdpServer.repository.DepartmentRepository;
import com.baotruongtuan.RdpServer.service.imp.IDepartmentService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DepartmentService implements IDepartmentService {
    DepartmentMapper departmentMapper;
    DepartmentRepository departmentRepository;
    DepartmentDetailRepository departmentDetailRepository;
    UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public DepartmentDTO createDepartment(DepartmentCreationRequest departmentCreationRequest) {
        Department department = departmentMapper.toDepartment(departmentCreationRequest);
        department.setCode();

        return departmentMapper.toDepartmentDTO(departmentRepository.save(department));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDepartmentDTO)
                .toList();
    }

    @Override
    public List<UserDTO> getMembersInDepartment(int departmentId) {
        return departmentDetailRepository.findAllByDepartmentId(departmentId).stream()
                .map(departmentDetail -> userMapper.toUserDTO(departmentDetail.getUser()))
                .toList();
    }
}
