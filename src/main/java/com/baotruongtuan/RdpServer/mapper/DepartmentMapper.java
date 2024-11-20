package com.baotruongtuan.RdpServer.mapper;

import org.mapstruct.Mapper;

import com.baotruongtuan.RdpServer.dto.DepartmentDTO;
import com.baotruongtuan.RdpServer.entity.Department;
import com.baotruongtuan.RdpServer.payload.request.DepartmentCreationRequest;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface DepartmentMapper {
    Department toDepartment(DepartmentCreationRequest departmentCreationRequest);

    DepartmentDTO toDepartmentDTO(Department department);
}
