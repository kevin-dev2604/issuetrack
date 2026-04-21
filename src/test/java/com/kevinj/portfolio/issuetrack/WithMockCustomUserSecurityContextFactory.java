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

        // 1. 실제 사용하시는 SecurityUserDetails 객체 생성 (NPE 방지를 위해 필드 채우기)
        SecurityUserDetails principal = new SecurityUserDetails(
            annotation.id(),
            annotation.loginId(),
            "password",
            annotation.role()
        );

        // 2. Authentication 객체 생성
        Authentication auth = new UsernamePasswordAuthenticationToken(
            principal,
            principal.getPassword(),
            principal.getAuthorities()
        );

        // 3. 컨텍스트에 주입
        context.setAuthentication(auth);
        return context;
    }
}
