package com.baotruongtuan.RdpServer.repository;

import com.baotruongtuan.RdpServer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
}
