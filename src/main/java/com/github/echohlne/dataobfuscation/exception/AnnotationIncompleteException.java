package com.github.echohlne.dataobfuscation.exception;

public class AnnotationIncompleteException extends RuntimeException{
    private static final long serialVersionUID = 17873183378187L;

    public AnnotationIncompleteException() {
    }

    public AnnotationIncompleteException(String message) {
        super(message);
    }

    public AnnotationIncompleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationIncompleteException(Throwable cause) {
        super(cause);
    }

    public AnnotationIncompleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
