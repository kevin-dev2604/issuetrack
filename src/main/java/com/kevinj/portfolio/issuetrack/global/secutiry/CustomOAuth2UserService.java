package com.kevinj.portfolio.issuetrack.global.secutiry;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.OAuth2LoginCommand;
import com.kevinj.portfolio.issuetrack.auth.application.usecase.OAuth2LoginUseCase;
import com.kevinj.portfolio.issuetrack.global.dto.oauth.CustomOAuth2User;
import com.kevinj.portfolio.issuetrack.global.dto.oauth.OAuthAttributes;
import com.kevinj.portfolio.issuetrack.user.application.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    // 내부 Core 영역의 포트를 주입받음
    private final OAuth2LoginUseCase loginUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 1. 구글의 기본 식별자 key 이름 추출 (기본값: "sub")
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 기존 가겨온 속성들을 수정 가능하도록 HashMap으로 복사 (불변 에러 방지)
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        // 깃허브인데 이메일이 null 이라면? 전용 API를 직접 찔러서 가져옵니다.
        if ("github".equals(registrationId) && attributes.get("email") == null) {
            String accessToken = userRequest.getAccessToken().getTokenValue(); // 깃허브가 준 토큰 추출
            String githubEmail = fetchGithubEmail(accessToken);

            // 찾아온 이메일을 맵에 강제로 주입!
            attributes.put("email", githubEmail);
        }

        // 유저 정보 파싱
        OAuthAttributes extractAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

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

    private String fetchGithubEmail(String accessToken) {
        try {
            List<Map<String, Object>> response = RestClient.create()
                .get()
                .uri("https://api.github.com/user/emails")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

            if (response != null) {
                // 여러 이메일 중 primary(대표) 이메일이면서 인증(verified)된 이메일을 찾아 반환
                return response.stream()
                    .filter(emailMap -> Boolean.TRUE.equals(emailMap.get("primary")))
                    .map(emailMap -> (String) emailMap.get("email"))
                    .findFirst()
                    .orElse(null);
            }
        } catch (Exception e) {
            // 로그를 남기거나 예외 처리
            log.error("깃허브 이메일 조회 실패: " + e.getMessage());
        }
        return null;
    }
}
