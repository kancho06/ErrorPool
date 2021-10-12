package com.sparta.errorpool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class SkillNullException extends RuntimeException{
    public SkillNullException(String message) {
        super(message);
    }
}
