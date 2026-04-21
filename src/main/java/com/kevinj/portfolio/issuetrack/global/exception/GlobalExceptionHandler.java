package com.kevinj.portfolio.issuetrack.global.exception;

import com.kevinj.portfolio.issuetrack.auth.exception.AuthException;
import com.kevinj.portfolio.issuetrack.auth.exception.UserNotFoundException;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DilemmaException;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DilemmaNotFoundException;
import com.kevinj.portfolio.issuetrack.dilemma.exception.DiscussionNotFoundException;
import com.kevinj.portfolio.issuetrack.global.exception.business.BusinessException;
import com.kevinj.portfolio.issuetrack.global.exception.business.ErrorCode;
import com.kevinj.portfolio.issuetrack.global.time.DateTimeUtils;
import com.kevinj.portfolio.issuetrack.global.time.SystemTimeProvider;
import com.kevinj.portfolio.issuetrack.global.time.TimeProvider;
import com.kevinj.portfolio.issuetrack.issue.exception.IssueException;
import com.kevinj.portfolio.issuetrack.issue.exception.IssueNotFoundException;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessAlreadyDeletedException;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessException;
import com.kevinj.portfolio.issuetrack.process.exception.process.ProcessNotFoundException;
import com.kevinj.portfolio.issuetrack.user.exception.DuplicatedEmailException;
import com.kevinj.portfolio.issuetrack.user.exception.DuplicatedLoginIdException;
import com.kevinj.portfolio.issuetrack.user.exception.NotFoundUserException;
import com.kevinj.portfolio.issuetrack.user.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleUserException(BusinessException ex, HttpServletRequest request) {
        int statusCode = 400;

        if (ex instanceof UserException) {
            if (ex instanceof DuplicatedEmailException || ex instanceof DuplicatedLoginIdException) {
                statusCode = 409;
            } else if (ex instanceof NotFoundUserException) {
                statusCode = 404;
            }

        } else if (ex instanceof AuthException) {
            if (ex instanceof UserNotFoundException) {
                statusCode = 404;
            } else {
                statusCode = 401;
            }

        } else if (ex instanceof ProcessException) {
            if (ex instanceof ProcessNotFoundException) {
                statusCode = 404;
            } else if (ex instanceof ProcessAlreadyDeletedException) {
                statusCode = 409;
            }

        } else if (ex instanceof IssueException) {
            if (ex instanceof IssueNotFoundException) {
                statusCode = 404;
            }

        } else if (ex instanceof DilemmaException) {
            if (ex instanceof DilemmaNotFoundException || ex instanceof DiscussionNotFoundException) {
                statusCode = 404;
            }

        }

        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(statusCode)
                .body(getBody(statusCode, errorCode.name(), errorCode.message(), request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleDefaults(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus.value())
                .body(getBody(httpStatus.value(), httpStatus.name(), httpStatus.getReasonPhrase(), request));
    }

    private ErrorResponse getBody(Integer statusCode, String code, String message, HttpServletRequest request) {
        TimeProvider timeProvider = new SystemTimeProvider();

        return new ErrorResponse(statusCode, code, message, request.getRequestURI(), DateTimeUtils.format(timeProvider.now()));
    }
}
