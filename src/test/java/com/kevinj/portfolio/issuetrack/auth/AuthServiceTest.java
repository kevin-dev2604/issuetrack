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
    void issue_accesstoken_and_refreshtoken_upon_successful_login() {
        LoginResponse response = authService.login(new LoginCommand(loginId, loginPw), "TEST");

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }

    @Test
    void save_the_refreshtoken_upon_successful_login() {
        LoginResponse response = authService.login(new LoginCommand(loginId, loginPw), "TEST");

        assertThat(fakeRefreshTokenStore.get(userId).isPresent()).isTrue();
        // 유즈케이스 테스트 단계이므로 Hash 처리 안함
        assertThat(fakeRefreshTokenStore.get(userId).get().tokenHash()).isEqualTo(response.refreshToken());
    }

    @Test
    void login_fails_if_password_does_not_match() {
        assertThatThrownBy(() -> authService.login(new LoginCommand(loginId, "rtu36%"), "TEST"))
                .isInstanceOf(PasswordDoNotMatchException.class);
    }

    @Test
    void login_fails_for_non_existent_ids() {
        LoginCommand command = new LoginCommand("unsigned_user", "rtu36%");

        assertThatThrownBy(() -> authService.login(command, "TEST"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void disabled_ids_fail_to_login() {
        User user = fakeUserPort.loadById(userId).get();
        user.inactive();

        assertThatThrownBy(() -> authService.login(new LoginCommand(loginId, loginPw), "TEST"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void save_logs_based_on_results_after_login() {
        authService.login(new LoginCommand(loginId, loginPw), "TEST");
        assertThat(fakeLoginLogPort.getLastLog().getIsSuccess()).isEqualTo(YN.Y);

        Throwable throwable = catchThrowable(() ->
                authService.login(new LoginCommand(loginId, "t3gg#"), "TEST"));
        assertThat(throwable).isInstanceOf(PasswordDoNotMatchException.class);
        assertThat(fakeLoginLogPort.getLastLog().getIsSuccess()).isEqualTo(YN.N);
    }

    @Test
    void reissue_token_with_a_valid_refreshtoken() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");
        RefreshResponse refreshResponse = authService.refresh(new RefreshCommand(loginResponse.refreshToken()));

        assertThat(refreshResponse).isNotNull();
        assertThat(refreshResponse.accessToken()).isNotBlank();
        assertThat(refreshResponse.refreshToken()).isNotEqualTo(loginResponse.refreshToken());
    }

    @Test
    void if_the_refresh_token_is_not_saved_reissuance_fails() {
        authService.login(new LoginCommand(loginId, loginPw), "TEST");

        String inValidRefreshToken = fakeTokenProvider.createRefreshToken(userId);

        assertThatThrownBy(() -> authService.refresh(new RefreshCommand(inValidRefreshToken)))
                .isInstanceOf(RefreshTokenInvalidException.class);
    }
    @Test
    void reissuing_a_token_with_an_accesstoken_fails() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");

        assertThatThrownBy(() -> authService.refresh(new RefreshCommand(loginResponse.accessToken())))
                .isInstanceOf(RefreshTokenInvalidException.class);
    }

    @Test
    void the_refresh_token_is_deleted_when_logout() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");
        authService.logout(new RefreshCommand(loginResponse.refreshToken()));

        assertThat(fakeRefreshTokenStore.get(userId).isEmpty()).isTrue();
    }

    @Test
    void reissuing_with_the_same_refreshtoken_after_logout_fails() {
        LoginResponse loginResponse = authService.login(new LoginCommand(loginId, loginPw), "TEST");
        RefreshCommand refreshCommand = new RefreshCommand(loginResponse.refreshToken());
        authService.logout(refreshCommand);

        assertThatThrownBy(() -> authService.refresh(refreshCommand))
                .isInstanceOf(RefreshTokenInvalidException.class);
    }

}
