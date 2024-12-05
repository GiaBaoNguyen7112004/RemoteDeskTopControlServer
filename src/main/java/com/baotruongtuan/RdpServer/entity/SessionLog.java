package com.baotruongtuan.RdpServer.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "session_logs")
public class SessionLog {
    @Id
    String id;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "sessionLog")
    List<SessionEvent> sessionEvents;

    public List<SessionEvent> getSessionEvents() {
        if (sessionEvents == null) sessionEvents = new ArrayList<>();
        return sessionEvents;
    }
}
