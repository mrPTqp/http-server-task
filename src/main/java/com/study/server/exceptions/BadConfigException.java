package com.study.server.exceptions;

public class BadConfigException extends RuntimeException {

    public BadConfigException(String message) {
        super(message);
    }
}
