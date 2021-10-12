package com.sparta.errorpool.globalController;


import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.StatusCode;
import com.sparta.errorpool.defaultResponse.SuccessYn;
import com.sparta.errorpool.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalController {

    @ExceptionHandler
    public ResponseEntity<String> DuplicateUserExceptionHandler(DuplicateUserException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> EmailFormExceptionHandler(EmailFormException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> UsernamePatternExceptionHandler(UsernamePatternException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> UsernameLengthExceptionHandler(UsernameLengthException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> PasswordLengthExceptionHandler(PasswordLengthException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> PasswordContainsExceptionHandler(PasswordContainsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    public ResponseEntity<String> articleNotFoundExceptionHandler(ArticleNotFoundException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.NOT_FOUND, exception.getMessage(), null), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity<String> accessDeniedExceptionHandler(AccessDeniedException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.FORBIDDEN, exception.getMessage(), null), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalAccessException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
    public ResponseEntity<String> CommentNotFoundExceptionHandler(CommentNotFoundException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.NOT_FOUND, exception.getMessage(), null), HttpStatus.OK);
    }
}
