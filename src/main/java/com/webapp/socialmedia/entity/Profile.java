package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_profile")
public class Profile {
    @Id
    @Column(name = "user_id")
    private String userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    private String bio;
    private String avatar;
    @Column(nullable = false)
    private String fullName;
}
