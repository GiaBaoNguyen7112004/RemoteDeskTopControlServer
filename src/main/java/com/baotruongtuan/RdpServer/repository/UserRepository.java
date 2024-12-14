package com.baotruongtuan.RdpServer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baotruongtuan.RdpServer.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM users u JOIN u.departmentDetails d WHERE d.department.id = :departmentId")
    List<User> findByDepartmentId(@Param("departmentId") int departmentId);
}
