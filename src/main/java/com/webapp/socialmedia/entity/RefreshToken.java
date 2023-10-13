package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
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

    @PrePersist
    private void createdAt() {
        Date dt = new Date();
        this.expireDate = new Date(dt.getTime() + (1000 * 60 * 60 * 24*7));
    }

}
