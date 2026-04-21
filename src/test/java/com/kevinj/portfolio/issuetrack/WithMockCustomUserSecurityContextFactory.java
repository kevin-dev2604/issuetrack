package com.kevinj.portfolio.issuetrack;

import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 1. Create the SecurityUserDetails object you will actually be using (populate fields to prevent NPE)
        SecurityUserDetails principal = new SecurityUserDetails(
            annotation.id(),
            annotation.loginId(),
            "password",
            annotation.role()
        );

        // 2. Create Authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken(
            principal,
            principal.getPassword(),
            principal.getAuthorities()
        );

        // 3. Inject into context
        context.setAuthentication(auth);
        return context;
    }
}
