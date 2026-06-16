package com.kevinj.portfolio.issuetrack.auth.adapter.in;

import com.kevinj.portfolio.issuetrack.global.secutiry.SecurityUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    @Operation(
        summary = "OAuth2 login info",
        description = "Show oauth2 login information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
        @ApiResponse(responseCode = "400", description = "oauth login failed"),
    })
    @GetMapping("/api/user")
    public Map<String, Object> getUserInfo(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return Map.of("status", "Not Authenticated");
        }

        SecurityUserDetails securityUserDetails = (SecurityUserDetails) userDetails;

        return Map.of(
            "userId", securityUserDetails.getUserId(),
            "role", securityUserDetails.getRole().systemRole()
        );
    }

}
