package com.baotruongtuan.RdpServer.repository;

import com.baotruongtuan.RdpServer.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
}
