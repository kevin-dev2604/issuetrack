package com.kevinj.portfolio.issuetrack.global.secutiry;

import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.jpa.Users;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Users user = jpaUserRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User\'s login id not found: " + username));

        return new SecurityUserDetails(user.getUserId(), user.getLoginId(), user.getLoginPw(), user.getUserRole());
    }
}
