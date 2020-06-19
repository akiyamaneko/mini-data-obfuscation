package com.github.echohlne.dataobfuscation.exception;


public class CopyException extends RuntimeException {
    public CopyException(String message) {
        super(message);
    }

    public CopyException(String message, Throwable cause) {
        super(message, cause);
    }

}
