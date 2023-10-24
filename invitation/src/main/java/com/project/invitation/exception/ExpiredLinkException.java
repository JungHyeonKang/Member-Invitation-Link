package com.project.invitation.exception;

public class ExpiredLinkException extends RuntimeException {
    public ExpiredLinkException() {
        super("만료된 링크입니다.");
    }

    public ExpiredLinkException(String message) {
        super(message);
    }

    public ExpiredLinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredLinkException(Throwable cause) {
        super(cause);
    }

    public ExpiredLinkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
