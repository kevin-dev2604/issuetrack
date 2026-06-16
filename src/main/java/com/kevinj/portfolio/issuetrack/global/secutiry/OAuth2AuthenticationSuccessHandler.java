package com.kevinj.portfolio.issuetrack.global.secutiry;

import com.kevinj.portfolio.issuetrack.global.dto.oauth.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // CustomOAuth2User에서 토큰 꺼내기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = oAuth2User.accessToken();
        String refreshToken = oAuth2User.refreshToken();

        // 프론트엔드 로그인 완료 페이지 주소로 토큰을 붙여서 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/redirect")
            .queryParam("accessToken", accessToken)
            .queryParam("refreshToken", refreshToken)
            .build().toUriString();

        // 프론트엔드로 브라우저 이동 시키기
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
