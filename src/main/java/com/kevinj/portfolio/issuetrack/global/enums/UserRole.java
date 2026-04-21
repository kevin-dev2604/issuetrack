package com.kevinj.portfolio.issuetrack.global.enums;

public enum UserRole {
    ADMIN,
    DILEMMA,
    USER
    ;

    UserRole() {}

    public String systemRole() {
        return "ROLE_" + this.name();
    }
}
