package com.sparta.errorpool.globalController;


import com.sparta.errorpool.defaultResponse.DefaultResponse;
import com.sparta.errorpool.defaultResponse.ResponseMessage;
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
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATE_EMAIL, null), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity<String> EmailFormExceptionHandler(EmailFormException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.EMAILFORM_ERROR, null), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity<String> UsernamePatternExceptionHandler(UsernamePatternException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.USERNAME_PATTERN, null), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity<String> UsernameLengthExceptionHandler(UsernameLengthException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.USERNAME_LENGTH, null), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity<String> PasswordLengthExceptionHandler(PasswordLengthException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.PASSWORD_LENGTH, null), HttpStatus.OK);
    }
    @ExceptionHandler
    public ResponseEntity<String> PasswordContainsExceptionHandler(PasswordContainsException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.BAD_REQUEST, ResponseMessage.PASSWORD_CONTAINS_ID, null), HttpStatus.OK);
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
    public ResponseEntity<String> unauthenticatedExceptionHandler(UnauthenticatedException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.UNAUTHORIZED, exception.getMessage(), null), HttpStatus.OK);
    }
    public ResponseEntity<String> tokenExpiredExceptionHandler(JwtTokenExpiredException exception) {
        return new ResponseEntity(DefaultResponse.res(SuccessYn.NO, StatusCode.UNAUTHORIZED, exception.getMessage(), null), HttpStatus.OK);
    }
}
