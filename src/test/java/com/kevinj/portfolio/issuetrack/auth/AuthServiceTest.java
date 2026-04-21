package com.kevinj.portfolio.issuetrack.auth;

import com.kevinj.portfolio.issuetrack.FakePasswordEncoder;
import com.kevinj.portfolio.issuetrack.auth.adapter.out.redis.RefreshTokenStore;
import com.kevinj.portfolio.issuetrack.auth.application.AuthService;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginCommand;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshCommand;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshResponse;
import com.kevinj.portfolio.issuetrack.auth.application.port.PasswordEncodePort;
import com.kevinj.portfolio.issuetrack.auth.exception.PasswordDoNotMatchException;
import com.kevinj.portfolio.issuetrack.auth.exception.RefreshTokenInvalidException;
import com.kevinj.portfolio.issuetrack.auth.exception.UserNotFoundException;
import com.kevinj.portfolio.issuetrack.auth.security.FakeRefreshTokenStore;
import com.kevinj.portfolio.issuetrack.auth.security.FakeTokenProvider;
import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.global.secutiry.TokenProvider;
import com.kevinj.portfolio.issuetrack.user.FakeUserPort;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AuthServiceTest {

    private final PasswordEncodePort fakePasswordEncoder = new FakePasswordEncoder();
    private final RefreshTokenStore fakeRefreshTokenStore = new FakeRefreshTokenStore();
    private final TokenProvider fakeTokenProvider = new FakeTokenProvider();
    private final FakeUserPort fakeUserPort = new FakeUserPort();
    private final FakeAuthPort fakeAuthPort = new FakeAuthPort(fakePasswordEncoder, fakeTokenProvider, fakeRefreshTokenStore);
    private final FakeLoginLogPort fakeLoginLogPort = new FakeLoginLogPort();

    private final AuthService authService = new AuthService(fakeUserPort, fakeAuthPort, fakeLoginLogPort);

    private Long userId;
    private String loginId;
    private String loginPw;

    @BeforeEach
    void setUp() {
        userId = fakeUserPort.newId();
        loginId = "kevin.j";
        loginPw = "qw123$";
        fakeUserPort.save(new User(
                userId,
                loginId,
                fakePasswordEncoder.encode(loginPw),
                UserRole.USER,
                "kevin",
                "test@kevin.com",
                "",
                YN.Y,
                0
        ));
    }

    @Test
    void 로그인_성공시_accessToken과_refreshToken을_발급한다() {
        LoginResponse response = authService.login(new LoginCommand(loginId, loginPw), "TEST");

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }

    @Test
    void 로그인_성공시_refreshToken을_저장한다() {
        LoginResponse response = authService.login(new LoginCommand(loginId, loginPw), "TEST");

        assertThat(fakeRefreshTokenStore.get(userId).isPresent()).isTrue();
        // 유즈케이스 테스트 단계이므로 Hash 처리 안함
        assertThat(fakeRefreshTokenStore.get(userId).get().tokenHash()).isEqualTo(response.refreshToken());
    }

    @Test
    void 비밀번호_불일치시_로그인_실패한다() {
        assertThatThrownBy(() -> authService.login(new LoginCommand(loginId, "rtu36%"), "TEST"))
                .isInstanceOf(PasswordDoNotMatchException.class);
    }

    @Test
    void 존재하지_않는_아이디는_로그인_실패한다() {
        LoginCommand command = new LoginCommand("unsigned_user", "rtu36%");

        assertThatThrownBy(() -> authService.login(command, "TEST"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 비활성화_아이디는_로그인_실패한다() {
        User user = fakeUserPort.loadById(userId).get();
        user.inactive();

        assertThatThrownBy(() -> authService.login(new LoginCommand(loginId, loginPw), "TEST"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 로그인_이후_결과에_따라_로그를_저장한다() {
        authService.login(new LoginCommand(loginId, loginPw), "TEST");
        assertThat(fakeLoginLogPort.getLastLog().getIsSuccess()).isEqualTo(YN.Y);

        Throwable throwable = catchThrowable(() ->
                authService.login(new LoginCommand(loginId, "t3gg#"), "TEST"));
        assertThat(throwable).isInstanceOf(PasswordDoNotMatchException.class);
        assertThat(fakeLoginLogPort.getLastLog().getIsSuccess()).isEqualTo(YN.N);
    }

    @Test
    void 유효한_refreshToken으로_토큰을_재발급한다() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");
        RefreshResponse refreshResponse = authService.refresh(new RefreshCommand(loginResponse.refreshToken()));

        assertThat(refreshResponse).isNotNull();
        assertThat(refreshResponse.accessToken()).isNotBlank();
        assertThat(refreshResponse.refreshToken()).isNotEqualTo(loginResponse.refreshToken());
    }

    @Test
    void 저장되지_않은_refreshToken이면_재발급에_실패한다() {
        authService.login(new LoginCommand(loginId, loginPw), "TEST");

        String inValidRefreshToken = fakeTokenProvider.createRefreshToken(userId);

        assertThatThrownBy(() -> authService.refresh(new RefreshCommand(inValidRefreshToken)))
                .isInstanceOf(RefreshTokenInvalidException.class);
    }
    @Test
    void accessToken으로_토근을_재발급하면_실패한다() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");

        assertThatThrownBy(() -> authService.refresh(new RefreshCommand(loginResponse.accessToken())))
                .isInstanceOf(RefreshTokenInvalidException.class);
    }

    @Test
    void 로그아웃하면_refreshToken이_삭제된다() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");
        authService.logout(new RefreshCommand(loginResponse.refreshToken()));

        assertThat(fakeRefreshTokenStore.get(userId).isEmpty()).isTrue();
    }

    @Test
    void 로그아웃_이후_같은_refreshToken으로_재발급하면_실패한다() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");
        RefreshCommand refreshCommand = new RefreshCommand(loginResponse.refreshToken());
        authService.logout(refreshCommand);

        assertThatThrownBy(() -> authService.refresh(refreshCommand))
                .isInstanceOf(RefreshTokenInvalidException.class);
    }

}
