package com.baotruongtuan.RdpServer.repository;

import com.baotruongtuan.RdpServer.entity.SessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, Long> {
}
