package com.baotruongtuan.RdpServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baotruongtuan.RdpServer.entity.SessionLog;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, String> {}
