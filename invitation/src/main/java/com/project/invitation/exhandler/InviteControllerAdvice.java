package com.project.invitation.exhandler;

import com.project.invitation.exception.ExistingMemberException;
import com.project.invitation.exception.ExpiredLinkException;
import com.project.invitation.exception.InvalidInviteException;
import com.project.invitation.exception.InvalidLinkException;
import com.project.invitation.exhandler.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class InviteControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidInviteException.class)
    public ErrorResponseDto handleInvalidInviteException(InvalidInviteException e) {
        return new ErrorResponseDto("400",e.getMessage(),e.getValidationErrors());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredLinkException.class)
    public ErrorResponseDto handleExpiredLinkException(ExpiredLinkException e) {
        return new ErrorResponseDto("400",e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidLinkException.class)
    public ErrorResponseDto handleInvalidLinkException(InvalidLinkException e) {
        return new ErrorResponseDto("404",e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExistingMemberException.class)
    public ErrorResponseDto handleExistingException(ExistingMemberException e) {
        return new ErrorResponseDto("400",e.getMessage());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponseDto handleIllegalException(IllegalArgumentException e) {
        return new ErrorResponseDto("400", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto handleException(Exception e) {
        return new ErrorResponseDto("500", e.getMessage());
    }

}
