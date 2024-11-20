package com.baotruongtuan.RdpServer.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "expired_tokens")
public class ExpiredToken {
    @Id
    String id;

    @Column(name = "expire_time")
    Date expireTime;
}
