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
    void membership_success_verification() {

        Optional<User> userOptional = userService.getLoginUserDomain("user1");
        assertThat(userOptional.isPresent()).isTrue();

        User user = userOptional.get();
        assertThat(user.getIsUse()).isEqualTo(YN.Y);
        assertThat(user.getLoginFailCnt()).isEqualTo(0);
        assertThat(user.getEmail()).isNotBlank();
    }

    @Test
    void sign_up_failed_due_to_missing_required_field() {
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
    void verification_of_password_encryption_status_upon_sign_up() {
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
    void sign_up_failed_with_an_existing_id() {
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
    void sign_up_failed_with_a_duplicate_email() {
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
    void registration_fails_if_password_and_email_format_do_not_match() {
        // The password rule is to be at least 5 characters long, consist of alphabets, numbers, and special characters, and include at least one special character.
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
    void failed_to_sign_up_using_a_withdrawn_user_s_id() {
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

}
