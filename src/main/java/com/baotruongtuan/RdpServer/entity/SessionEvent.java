package com.baotruongtuan.RdpServer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "session_events")
public class SessionEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String title;
    LocalDateTime time;
    String content;

    @Builder.Default
    String author = "server";

    @ManyToOne
    SessionLog sessionLog;
}
