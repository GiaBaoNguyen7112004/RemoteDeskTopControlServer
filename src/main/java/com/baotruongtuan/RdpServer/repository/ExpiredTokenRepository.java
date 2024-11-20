package com.baotruongtuan.RdpServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baotruongtuan.RdpServer.entity.ExpiredToken;

public interface ExpiredTokenRepository extends JpaRepository<ExpiredToken, String> {}
