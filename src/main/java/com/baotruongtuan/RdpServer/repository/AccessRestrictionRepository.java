package com.baotruongtuan.RdpServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baotruongtuan.RdpServer.entity.AccessRestriction;

@Repository
public interface AccessRestrictionRepository extends JpaRepository<AccessRestriction, String> {
    boolean existsByDomain(String domain);

    boolean existsByApp(String app);
}
