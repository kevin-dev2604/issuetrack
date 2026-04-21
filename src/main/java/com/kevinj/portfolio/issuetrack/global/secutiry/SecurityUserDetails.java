package com.kevinj.portfolio.issuetrack.global.secutiry;

import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class SecurityUserDetails implements UserDetails, Serializable {

    @Getter
    private final Long userId;
    private final String loginId;
    private final String password;
    private final UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.systemRole()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

}
