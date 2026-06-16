package com.kevinj.portfolio.issuetrack.global.dto.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public record CustomOAuth2User(
    Collection<? extends GrantedAuthority> authorities,
    Map<String, Object> attributes,
    String nameAttributeKey,
    String accessToken,  // 우리 서비스 전용 JWT
    String refreshToken // 우리 서비스 전용 JWT
) implements OAuth2User {


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get(nameAttributeKey));
    }
}
