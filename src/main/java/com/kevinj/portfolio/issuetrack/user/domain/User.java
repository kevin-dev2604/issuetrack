package com.kevinj.portfolio.issuetrack.user.domain;

import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserUpdateCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private Long userId;
    private String loginId;
    private String loginPw;
    private UserRole userRole;
    private String nickname;
    private String email;
    private String details;
    private YN isUse;
    private Integer loginFailCnt;

    public void inactive() {
        this.isUse = YN.N;
    }

    public void updateInfo(UserUpdateCommand userManageCreateCommand) {
        this.nickname = userManageCreateCommand.nickname();
        this.email = userManageCreateCommand.email();
        this.details = userManageCreateCommand.details();
    }

    public void changePassword(String newPassword) {
        this.loginPw = newPassword;
    }

    public static User create(String loginId, String loginPw, String nickname, String email, String details) {
        return new User(null, loginId, loginPw, UserRole.USER, nickname, email, details, YN.Y, 0);
    }

    public void addLoginFailCnt() {
        this.loginFailCnt++;
    }
}
