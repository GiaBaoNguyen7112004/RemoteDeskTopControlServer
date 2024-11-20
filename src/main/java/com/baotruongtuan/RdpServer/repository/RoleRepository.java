package com.baotruongtuan.RdpServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baotruongtuan.RdpServer.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {}
