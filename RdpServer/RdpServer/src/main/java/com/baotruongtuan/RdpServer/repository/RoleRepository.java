package com.baotruongtuan.RdpServer.repository;

import com.baotruongtuan.RdpServer.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
