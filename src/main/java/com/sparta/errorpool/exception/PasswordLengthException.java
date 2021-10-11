package com.sparta.errorpool.exception;

public class PasswordLengthException extends RuntimeException{
    public PasswordLengthException(String message) {
        super(message);
    }
}
