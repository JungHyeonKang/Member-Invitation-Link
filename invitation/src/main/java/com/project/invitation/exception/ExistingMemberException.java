package com.project.invitation.exception;

public class ExistingMemberException extends RuntimeException{
    public ExistingMemberException() {
        super("이미 가입한 회원입니다.");
    }

    public ExistingMemberException(String message) {
        super(message);
    }

    public ExistingMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingMemberException(Throwable cause) {
        super(cause);
    }

    public ExistingMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
