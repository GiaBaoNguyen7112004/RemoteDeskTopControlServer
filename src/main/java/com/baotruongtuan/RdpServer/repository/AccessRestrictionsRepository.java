package com.baotruongtuan.RdpServer.repository;

import com.baotruongtuan.RdpServer.entity.AccessRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRestrictionsRepository extends JpaRepository<AccessRestriction, String> {
    boolean existsAccessRestrictionByContent(String content);
}
