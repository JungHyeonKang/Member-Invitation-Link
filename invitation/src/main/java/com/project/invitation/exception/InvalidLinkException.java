package com.project.invitation.exception;


public class InvalidLinkException extends RuntimeException {
    public InvalidLinkException() {
        super("유효하지 않은 링크입니다.");
    }

    public InvalidLinkException(String message) {
        super(message);
    }

    public InvalidLinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLinkException(Throwable cause) {
        super(cause);
    }

    public InvalidLinkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
