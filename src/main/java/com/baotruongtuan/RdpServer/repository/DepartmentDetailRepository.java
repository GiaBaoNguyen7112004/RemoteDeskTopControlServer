package com.baotruongtuan.RdpServer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baotruongtuan.RdpServer.entity.DepartmentDetail;

public interface DepartmentDetailRepository extends JpaRepository<DepartmentDetail, Integer> {
    List<DepartmentDetail> findAllByDepartmentId(int departmentId);

    boolean existsByUserIdAndDepartmentCode(int userId, String code);

    Optional<DepartmentDetail> findByUserIdAndDepartmentId(int userId, int departmentId);

    void removeByUserIdAndDepartmentId(int userId, int departmentId);
}
