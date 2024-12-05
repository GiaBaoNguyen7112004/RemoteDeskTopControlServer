package com.baotruongtuan.RdpServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "banned_domains")
public class AccessRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String app;
    String domain;
}
