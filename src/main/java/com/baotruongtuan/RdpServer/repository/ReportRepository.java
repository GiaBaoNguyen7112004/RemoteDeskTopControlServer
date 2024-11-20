package com.baotruongtuan.RdpServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baotruongtuan.RdpServer.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {}
