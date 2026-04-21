package com.kevinj.portfolio.issuetrack;

import com.kevinj.portfolio.issuetrack.auth.application.port.PasswordEncodePort;

public class FakePasswordEncoder implements PasswordEncodePort {

    @Override
    public String encode(String rawPassword) {
        return String.format("ENC(@@%s@@)", rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return String.format("ENC(@@%s@@)", rawPassword).equals(encodedPassword);
    }
}
