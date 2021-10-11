package com.sparta.errorpool.exception;

public class UsernameLengthException extends RuntimeException{
    public UsernameLengthException(String message) {
        super(message);
    }
}
