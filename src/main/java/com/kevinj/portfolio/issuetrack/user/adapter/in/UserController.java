package com.kevinj.portfolio.issuetrack.user.adapter.in;

import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import com.kevinj.portfolio.issuetrack.user.application.UserService;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserCreateCommand;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserInfoResponse;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserPasswordCommand;
import com.kevinj.portfolio.issuetrack.user.application.dto.UserUpdateCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "User signup",
        description = "Create user account"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "input validation check failed"),
    })
    @PostMapping("/signup")
    public void signup(@RequestBody UserCreateCommand userCreateCommand) {
        userService.signUp(userCreateCommand);
    }

    @Operation(
        summary = "Delete user",
        description = "Delete user account"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found login user info"),
    })
    @DeleteMapping("/delete")
    public void deleteUser(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        userService.deleteUser(userInfo.getUserId());
    }

    @Operation(
        summary = "Update user",
        description = "Update user information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found login user info"),
    })
    @PostMapping("/update")
    public void updateUser(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                           @RequestBody UserUpdateCommand userUpdateCommand) {
        userService.updateUser(userInfo.getUserId(), userUpdateCommand);
    }

    @Operation(
        summary = "Change password",
        description = "Change user password to new"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found login user info"),
        @ApiResponse(responseCode = "400", description = "password check failed"),
    })
    @PostMapping("/newpassword")
    public void changePassword(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                               @RequestBody UserPasswordCommand userPasswordCommand) {
        userService.changePassword(userInfo.getUserId(), userPasswordCommand);
    }

    @Operation(
        summary = "Get user infomation",
        description = "Get infomation of login user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "404", description = "not found login user info"),
    })
    @GetMapping("/info")
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        return userService.getUserInfo(userInfo.getUserId());
    }
}
