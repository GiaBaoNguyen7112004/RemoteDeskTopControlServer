package com.baotruongtuan.RdpServer.entity;

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
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
    String password;
    String username;
    String email;
    String jobTitle;
    String companyName;

    @JoinColumn(name = "avatar_id")
    @OneToOne
    Avatar avatar;

    @ManyToOne
    Role role;

    @OneToMany(mappedBy = "user")
    List<SessionLog> sessionLogs;

    @OneToMany(mappedBy = "user")
    List<DepartmentDetail> departmentDetails;

    public void leaveDepartment(int departmentId) {
        this.getDepartmentDetails()
                .removeAll(this.getDepartmentDetails().stream()
                        .filter(departmentDetail ->
                                departmentDetail.getDepartment().getId() == departmentId
                                        && departmentDetail.getUser().getId() == this.getId())
                        .toList());
    }
}
