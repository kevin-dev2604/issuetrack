package com.kevinj.portfolio.issuetrack.auth.application.port;

public interface PasswordEncodePort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
