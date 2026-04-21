package com.kevinj.portfolio.issuetrack.user;

import com.kevinj.portfolio.issuetrack.FakePasswordEncoder;
import com.kevinj.portfolio.issuetrack.auth.application.port.PasswordEncodePort;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.application.UserService;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserCreateCommand;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import com.kevinj.portfolio.issuetrack.user.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTest {

    private final FakeUserPort fakeUserPort = new FakeUserPort();
    private final PasswordEncodePort fakePasswordEncoder = new FakePasswordEncoder();
    private final UserService userService = new UserService(fakeUserPort, fakePasswordEncoder);

    private Long userId;

    @BeforeEach
    void setUp() {
        UserCreateCommand userCreateCommand = new UserCreateCommand(
                "user1",
                "qwer12345%^",
                "tester 1",
                "test@kevinj.com",
                ""
        );

        userService.signUp(userCreateCommand);
        userId = fakeUserPort.lastId();
    }

    @Test
    void 회원가입_성공_검증() {

        Optional<User> userOptional = userService.getLoginUserDomain("user1");
        assertThat(userOptional.isPresent()).isTrue();

        User user = userOptional.get();
        assertThat(user.getIsUse()).isEqualTo(YN.Y);
        assertThat(user.getLoginFailCnt()).isEqualTo(0);
        assertThat(user.getEmail()).isNotBlank();
    }

    @Test
    void 필수값_누락하여_가입시_실패() {
        assertThatThrownBy(() -> userService.signUp(
                new UserCreateCommand(
                        "",
                        "invalidpw@",
                        "invalid user",
                        "invalid@kevinj.com",
                        ""
                )
        )).isInstanceOf(NoLoginIdInputException.class);

        assertThatThrownBy(() -> userService.signUp(
                new UserCreateCommand(
                        "invalid_account",
                        " ",
                        "invalid user",
                        "invalid@kevinj.com",
                        ""
                )
        )).isInstanceOf(NoPasswordInputException.class);

        assertThatThrownBy(() -> userService.signUp(
                new UserCreateCommand(
                        "invalid_account",
                        "invalidpw@",
                        "",
                        "invalid@kevinj.com",
                        ""
                )
        )).isInstanceOf(NoNicknameInputException.class);

        assertThatThrownBy(() -> userService.signUp(
                new UserCreateCommand(
                        "invalid_account",
                        "invalidpw@",
                        "invalid user",
                        "",
                        ""
                )
        )).isInstanceOf(NoEmailInputException.class);

    }

    @Test
    void 회원가입시_비밀번호_암호화_저장여부_검증() {
        String originalPwd = "fqor3t#";
        UserCreateCommand userCreateCommand = new UserCreateCommand(
                "user2",
                originalPwd,
                "another tester",
                "test2@kevinj.com",
                ""
        );

        userService.signUp(userCreateCommand);

        User user = userService.getLoginUserDomain(userCreateCommand.loginId()).get();

        assertThat(user.getLoginPw())
                .isNotEqualTo(originalPwd)
                .isEqualTo(String.format("ENC(@@%s@@)", originalPwd));
    }

    @Test
    void 존재하는_아이디로_가입시_실패() {
        UserCreateCommand userCreateCommand = new UserCreateCommand(
                "user1",
                "swq23%",
                "another tester",
                "test2@kevinj.com",
                ""
        );

        assertThatThrownBy(() -> userService.signUp(userCreateCommand))
                .isInstanceOf(DuplicatedLoginIdException.class);
    }

    @Test
    void 중복된_이메일로_가입시_실패() {
        UserCreateCommand userCreateCommand = new UserCreateCommand(
                "user4",
                "qwe123^",
                "same email",
                "test@kevinj.com",
                ""
        );

        assertThatThrownBy(() -> userService.signUp(userCreateCommand))
                .isInstanceOf(DuplicatedEmailException.class);
    }

    @Test
    void 비밀번호_및_이메일_형식이_안맞으면_회원가입_실패() {
        // 패스워드 규칙은 5글자 이상 & 알파벳, 숫자, 특수기호로 구성 & 특수기호가 1개 이상 포함
//        String rawPassword = "qwer12@";
        String rawPassword = "qwer";
//        String rawPassword = "qwer12";
//        String rawPassword = "q1^";
        String email = "test@tester.com";

        UserCreateCommand userCreateCommand = new UserCreateCommand(
                "user2",
                rawPassword,
                "another tester",
                email,
                ""
        );

        assertThatThrownBy(() -> userService.signUp(userCreateCommand))
                .isInstanceOf(WrongPasswordException.class);

        rawPassword = "qwer12@";
        email = "test@tester";

        UserCreateCommand userCreateCommand2 = new UserCreateCommand(
                "user3",
                rawPassword,
                "another tester 2",
                email,
                ""
        );

        assertThatThrownBy(() -> userService.signUp(userCreateCommand2))
                .isInstanceOf(WrongEmailException.class);

    }

    @Test
    void 탈퇴한_사용자의_아이디로_가입_신청시_실패() {
        userService.deleteUser(userId);

        UserCreateCommand userCreateCommand = new UserCreateCommand(
                "user1",
                "swq23%",
                "tester",
                "test1@kevinj.com",
                ""
        );

        assertThatThrownBy(() -> userService.signUp(userCreateCommand))
                .isInstanceOf(WithdrawnIdAccessException.class);
    }

//    @Test
//    void 회원_비활성화() {
//
//        User user1 = userService.getLoginUserDomain("user1").get();
//
//        user1.inactive();
//
//        assertThat(user1.getIsUse()).isEqualTo(YN.N);
//    }
//
//    @Test
//    void 회원정보_수정()  {
//
//        User user1 = userService.getLoginUserDomain("user1").get();
//
//        UserUpdateCommand command = new UserUpdateCommand(user1.getUserId(), "anotherUser", "another_user@kevinj.org", "another user");
//
//        user1.updateInfo(command);
//
//        assertThat(user1.getNickname()).isEqualTo(command.nickname());
//        assertThat(user1.getEmail()).isEqualTo(command.email());
//        assertThat(user1.getDetails()).isEqualTo(command.details());
//    }
//
//    @Test
//    void 비밀번호_변경() {
//        User user1 = userService.getLoginUserDomain("user1").get();
//
//        String newPassword = fakePasswordEncoder.encode("emd3dm2893");
//        user1.changePassword(newPassword);
//
//        assertThat(user1.getLoginPw()).isEqualTo(newPassword);
//    }
}
