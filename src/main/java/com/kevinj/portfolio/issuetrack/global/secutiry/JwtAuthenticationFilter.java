package com.kevinj.portfolio.issuetrack.global.secutiry;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtTokenProvider.validateToken(token)) {
                String loginId = jwtTokenProvider.getLoginId(token);
                String role = jwtTokenProvider.getRole(token);

                UserDetails principal = userDetailsService.loadUserByUsername(loginId);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        principal
                        , null
                        , List.of(new SimpleGrantedAuthority(role))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 필터 타지 않는 uri: 로그인, 회원가입, dilemma 검색, dilemma 상세조회
        return path.startsWith("/auth/")
            || List.of("/users/signup", "/dilemma/search").contains(path)
            || path.matches("^/dilemma/[0-9]+$");
    }

}
