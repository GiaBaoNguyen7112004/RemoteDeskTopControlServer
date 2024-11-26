package com.baotruongtuan.RdpServer.repository;

import com.baotruongtuan.RdpServer.entity.SessionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionMessageRepository extends JpaRepository<SessionMessage, Long> {
}
