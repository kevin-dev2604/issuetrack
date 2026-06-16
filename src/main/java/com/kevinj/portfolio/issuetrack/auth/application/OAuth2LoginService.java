package com.kevinj.portfolio.issuetrack.auth.application;

import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.OAuth2LoginCommand;
import com.kevinj.portfolio.issuetrack.auth.application.port.AuthPort;
import com.kevinj.portfolio.issuetrack.auth.application.usecase.OAuth2LoginUseCase;
import com.kevinj.portfolio.issuetrack.global.dto.oauth.CustomOAuth2User;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserUpdateCommand;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.kevinj.portfolio.issuetrack.admin.adapter.out.jpa.QAttributes.attributes;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2LoginService implements OAuth2LoginUseCase {

    private final UserPort userPort;
    private final AuthPort authPort;

    @Override
    public LoginResponse loginOrRegister(OAuth2LoginCommand command) {

        // 3. 비즈니스 로직(UseCase) 호출! (프레임워크 세계 탈출, 도메인 세계 진입)
        // 3-1. 기존 유저 조회 (이메일 + 제공자 조합으로 고유성 검증)
        Optional<User> user = userPort.loadByProvider(command.email(), command.provider());
        User userDomain;

        if (user.isEmpty()) {
            // 3-2-a. 최초 로그인이라면 신규 회원으로 정의 (도메인 엔티티 생성)
            userDomain = User.create(
                command.email(),
                "",
                command.name(),
                command.email(),
                null,
                command.provider()
            );

            // 공급자의 유저 정보를 영속성 컨텍스트(DB)에 저장
            userPort.create(userDomain);

        } else {
            // 3-2-b. 기존 유저라면 소셜 정보와 이름 변경 등이 있었는지 동기화 (도메인 비즈니스 메서드 호출)
            userDomain = user.get();

            userDomain.updateInfo(new UserUpdateCommand(
                command.name(),
                command.email(),
                userDomain.getDetails()
            ));

            // 변경된 유저 정보를 영속성 컨텍스트(DB)에 저장
            userPort.save(userDomain);
        }

        // 4. 변경되거나 신규 생성된 유저 정보를 영속성 컨텍스트(DB)와 동기화
        userDomain = userPort.loadByProvider(command.email(), command.provider())
            .orElseThrow(() -> new EntityNotFoundException(command.email()));

        // 5. CustomOAuth2User에 우리 서비스의 JWT 토큰을 담아서 반환
        return authPort.createLoginResponse(userDomain);
    }
}
