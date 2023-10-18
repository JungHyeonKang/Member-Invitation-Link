package com.project.invitation.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class InvalidInviteException extends RuntimeException {
    private List<String> validationErrors;

    public InvalidInviteException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public InvalidInviteException(String message, Throwable cause, List<String> validationErrors) {
        super(message, cause);
        this.validationErrors = validationErrors;
    }

    public InvalidInviteException(Throwable cause, List<String> validationErrors) {
        super(cause);
        this.validationErrors = validationErrors;
    }

    public InvalidInviteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, List<String> validationErrors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
