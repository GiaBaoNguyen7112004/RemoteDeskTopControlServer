package com.baotruongtuan.RdpServer.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "dob")
    LocalDate dob;

    @Column(name = "password")
    String password;

    @Column(name = "username")
    String username;

    @Column(name = "email")
    String email;

    @JoinColumn(name = "role_id")
    @ManyToOne
    Role role;

    @OneToMany(mappedBy = "user")
    Set<Report> reports;
}
