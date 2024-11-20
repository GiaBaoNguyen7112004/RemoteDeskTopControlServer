package com.baotruongtuan.RdpServer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baotruongtuan.RdpServer.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByCode(String code);
}
