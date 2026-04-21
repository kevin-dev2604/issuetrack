package com.kevinj.portfolio.issuetrack.user.adapter.out.jpa;

import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.persistence.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String loginPw;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    private String details;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private YN isUse;

    @Column(nullable = false)
    private Integer loginFailCnt;

    public Users(String loginId, String loginPw, UserRole userRole, String nickname, String email, String details, YN isUse, Integer loginFailCnt) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.userRole = userRole;
        this.nickname = nickname;
        this.email = email;
        this.details = details;
        this.isUse = isUse;
        this.loginFailCnt = loginFailCnt;
    }

    public void update(String nickname, String email, String details, YN isUse) {
        this.nickname = nickname;
        this.email = email;
        this.details = details;
        this.isUse = isUse;
    }
}
