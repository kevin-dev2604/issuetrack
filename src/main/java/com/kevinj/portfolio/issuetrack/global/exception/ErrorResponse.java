package com.kevinj.portfolio.issuetrack.global.exception;

public record ErrorResponse(
        int status,
        String code,
        String message,
        String path,
        String timestamp
) {}