package com.sparta.errorpool.exception;


public class PasswordContainsException extends RuntimeException{
    public PasswordContainsException(String message) {
        super(message);
    }
}
