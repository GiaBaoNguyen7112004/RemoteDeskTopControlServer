package com.baotruongtuan.RdpServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baotruongtuan.RdpServer.entity.ExpiredToken;

@Repository
public interface ExpiredTokenRepository extends JpaRepository<ExpiredToken, String> {}
