package com.kevinj.portfolio.issuetrack.admin.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    @Operation(
        summary = "health check",
        description = "only for health checks"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "success"),
    })
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

}
