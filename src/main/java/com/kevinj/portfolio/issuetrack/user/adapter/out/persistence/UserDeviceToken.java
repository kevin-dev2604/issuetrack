package com.kevinj.portfolio.issuetrack.user.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users users;

    @Unique
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String deviceType;

    @Column(nullable = false)
    private LocalDateTime lastLoggedInAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserDeviceToken(Users users, @Unique String token, String deviceType, LocalDateTime lastLoggedInAt) {
        this.users = users;
        this.token = token;
        this.deviceType = deviceType;
        this.lastLoggedInAt = lastLoggedInAt;
    }
}
