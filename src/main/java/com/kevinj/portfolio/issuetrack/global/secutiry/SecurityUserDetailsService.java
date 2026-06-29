package com.kevinj.portfolio.issuetrack.global.secutiry;

import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.JpaUserRepository;
import com.kevinj.portfolio.issuetrack.user.adapter.out.persistence.Users;
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
    public UserDetails loadUserByUsername(@NonNull String userId) throws UsernameNotFoundException {
        Users user = jpaUserRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User\'s user id not found: " + userId));

        return new SecurityUserDetails(user.getUserId(), user.getLoginId(), user.getLoginPw(), user.getUserRole(), user.getProvider());
    }


}
