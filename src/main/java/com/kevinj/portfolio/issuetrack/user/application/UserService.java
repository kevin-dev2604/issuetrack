package com.kevinj.portfolio.issuetrack.user.application;

import com.kevinj.portfolio.issuetrack.auth.application.port.PasswordEncodePort;
import com.kevinj.portfolio.issuetrack.global.enums.YN;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserCreateCommand;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserInfoResponse;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserPasswordCommand;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserUpdateCommand;
import com.kevinj.portfolio.issuetrack.user.application.port.UserPort;
import com.kevinj.portfolio.issuetrack.user.domain.ServiceDomain;
import com.kevinj.portfolio.issuetrack.user.domain.User;
import com.kevinj.portfolio.issuetrack.user.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserUseCase {

    private final UserPort userPort;
    private final PasswordEncodePort passwordEncodePort;

    @Override
    public void signUp(UserCreateCommand command) {
        validateSignUpCommand(command);

        String encodedPassword = passwordEncodePort.encode(command.loginPw());

        User user = User.create(
                command.loginId(),
                encodedPassword,
                command.nickname(),
                command.email(),
                command.details()
        );

        userPort.create(user);
    }

    private void validateSignUpCommand(UserCreateCommand command) {
        final String PWD_REGEX = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{5,}$";
        final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        if (command == null) {
            throw new NoInputException();

        } else if (command.loginId() == null || command.loginId().isBlank()) {
            throw new NoLoginIdInputException();

        } else if (command.loginPw() == null || command.loginPw().isBlank()) {
            throw new NoPasswordInputException();

        } else if (command.nickname() == null || command.nickname().isBlank()) {
            throw new NoNicknameInputException();

        } else if (command.email() == null || command.email().isBlank()) {
            throw new NoEmailInputException();

        } else if (userPort.loadLoginUser(command.loginId()).isPresent()) {
            User checkUser = userPort.loadLoginUser(command.loginId()).get();

            if (checkUser.getIsUse().equals(YN.N)) {
                throw new WithdrawnIdAccessException();
            }

            throw new DuplicatedLoginIdException();

        } else if (!command.loginPw().matches(PWD_REGEX)) {
            throw new WrongPasswordException();

        } else if (!command.email().matches(EMAIL_REGEX)) {
            throw new WrongEmailException();

        } else if (userPort.loadByEmail(command.email()).isPresent()) {
            throw new DuplicatedEmailException();
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userPort.loadById(userId)
                .orElseThrow(NotFoundUserException::new);

        user.inactive();
        userPort.save(user);
    }

    @Override
    public void updateUser(Long userId, UserUpdateCommand command) {
        User user = userPort.loadById(userId)
                .orElseThrow(NotFoundUserException::new);

        user.updateInfo(command);
        userPort.save(user);
    }

    @Override
    public void changePassword(Long userId, UserPasswordCommand command) {
        final String PWD_REGEX = "^(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{5,}$";

        User user = userPort.loadById(userId)
                .orElseThrow(NotFoundUserException::new);

        String encodedPassword = passwordEncodePort.encode(command.loginPw());

        if (command.loginPw() == null || command.loginPw().isBlank()
                || command.newloginPw() == null || command.newloginPw().isBlank()) {
            throw new NoPasswordInputException();
        } else if (command.loginPw().equals(command.newloginPw())) {
            throw new SamePasswordException();
        } else if (!encodedPassword.equals(user.getLoginPw())) {
            throw new WrongOldPasswordException();
        } else if (!command.newloginPw().matches(PWD_REGEX)) {
            throw new WrongPasswordException();
        }

        user.changePassword(passwordEncodePort.encode(command.newloginPw()));
        userPort.save(user);
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userPort.loadById(userId)
                .orElseThrow(NotFoundUserException::new);

        return new UserInfoResponse(user.getLoginId(),  user.getNickname(), user.getEmail(), user.getDetails());
    }

    @Override
    public ServiceDomain getServiceInfo(Long userId) {
        return userPort.loadServiceInfo(userId);
    }

    @Override
    public Optional<User> getLoginUserDomain(String loginId) {
        return userPort.loadLoginUser(loginId);
    }

    @Override
    public void addLoginFailCnt(Long userId) {
        Optional<User> user = userPort.loadById(userId);
        user.ifPresent(userDomain -> {
            userDomain.addLoginFailCnt();
            userPort.save(userDomain);
        });
    }
}
