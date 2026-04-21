package com.kevinj.portfolio.issuetrack.auth.adapter.in;

import com.kevinj.portfolio.issuetrack.auth.application.AuthService;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginCommand;
import com.kevinj.portfolio.issuetrack.auth.application.dto.LoginResponse;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshCommand;
import com.kevinj.portfolio.issuetrack.auth.application.dto.RefreshResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(
        summary = "User login",
        description = "Login with loginId and password"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "401", description = "login password does not matched"),
        @ApiResponse(responseCode = "404", description = "login user account not found"),
    })
    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginCommand loginCommand,
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = "X-Platform", required = false) String appPlatform
    ) {
        String browserInfo = Optional.ofNullable(userAgent).orElse(appPlatform);
        return authService.login(loginCommand, browserInfo);
    }

    @Operation(
        summary = "User logout",
        description = "Logout user and invalidate refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @PostMapping("/logout")
    public void logout(@RequestBody RefreshCommand refreshCommand) {
        authService.logout(refreshCommand);
    }

    @Operation(
        summary = "Refresh user tokens",
        description = "Refresh tokens with current refresh token"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "401", description = "current refresh token is invalid"),
    })
    @PostMapping("/refresh")
    public RefreshResponse refresh(@RequestBody RefreshCommand refreshCommand) {
        return authService.refresh(refreshCommand);
    }
}
