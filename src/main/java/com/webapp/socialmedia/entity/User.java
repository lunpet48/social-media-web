package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User {
    @Id
    @UuidGenerator
    private String id;
//    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
//    private String phone;
    private Boolean isDeleted = false;
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    private void createdAt() {
        this.createdAt = this.updatedAt = new Date();
    }

    @PreUpdate
    private void updatedAt() {
        this.updatedAt = new Date();
    }
}
