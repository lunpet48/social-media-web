package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_refresh_token")
public class RefreshToken {
    @Id
    private String id;
    @Column(nullable = false)
    private String familyId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Builder.Default
    private Boolean isUsed = false;
    @Builder.Default
    private Boolean isRevoked = false;
    private Date expireDate;
}
