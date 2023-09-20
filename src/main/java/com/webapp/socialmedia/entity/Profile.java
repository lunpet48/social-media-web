package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_profile")
public class Profile {
    @Id
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    private String bio;
    private String avatar;
    @Column(nullable = false)
    private String fullName;
}
