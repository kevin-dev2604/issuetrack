package com.kevinj.portfolio.issuetrack.global.secutiry;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.OAuth2LoginCommand;
import com.kevinj.portfolio.issuetrack.auth.application.usecase.OAuth2LoginUseCase;
import com.kevinj.portfolio.issuetrack.global.dto.oauth.CustomOAuth2User;
import com.kevinj.portfolio.issuetrack.global.dto.oauth.OAuthAttributes;
import com.kevinj.portfolio.issuetrack.user.application.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // 내부 Core 영역의 포트를 주입받음
    private final OAuth2LoginUseCase loginUseCase;
    private final UserUseCase userUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 1. 구글의 기본 식별자 key 이름 추출 (기본값: "sub")
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 구글 유저 정보 파싱
        OAuthAttributes extractAttributes = OAuthAttributes.ofGoogle(userNameAttributeName, oAuth2User.getAttributes());

        // 2. 외부 프레임워크 객체를 내부 도메인용 Command(DTO)로 변환
        OAuth2LoginCommand command = new OAuth2LoginCommand(
            extractAttributes.email(),
            extractAttributes.name(),
            registrationId
        );

        // 3. 비즈니스 로직(UseCase) 호출! (프레임워크 세계 탈출, 도메인 세계 진입)
        LoginResponse result = loginUseCase.loginOrRegister(command);

        // 4. CustomOAuth2User에 우리 서비스의 JWT 토큰을 담아서 반환
        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(result.role())),
            extractAttributes.attributes(),
            extractAttributes.nameAttributeKey(),
            result.accessToken(),
            result.refreshToken()
        );
    }
}
