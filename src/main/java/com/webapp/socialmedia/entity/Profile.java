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
@Table(name = "_prrofile")
public class Profile {
    @Id
    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private User userId;
    private String bio;
    private String avatar;
    private String fullName;
}
