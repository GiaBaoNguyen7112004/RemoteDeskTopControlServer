package com.baotruongtuan.RdpServer.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "session_messages")
public class SessionMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    LocalDateTime time;
    String content;

    @ManyToOne
    SessionLog sessionLog;
}
