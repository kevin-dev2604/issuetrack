package com.kevinj.portfolio.issuetrack.auth.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long traceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users users;

    @Column(nullable = false)
    private LocalDateTime loginTime;

    @Column(nullable = false)
    private String clientType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isSuccess;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public LoginLog(Users users, YN isSuccess, LocalDateTime loginTime, String clientType) {
        this.users = users;
        this.isSuccess = isSuccess;
        this.loginTime = loginTime;
        this.clientType = clientType;
    }
}
