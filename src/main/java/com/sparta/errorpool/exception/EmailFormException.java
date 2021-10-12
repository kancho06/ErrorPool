package com.sparta.errorpool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class EmailFormException extends RuntimeException{
    public EmailFormException(String message) {
        super(message);
    }
}
