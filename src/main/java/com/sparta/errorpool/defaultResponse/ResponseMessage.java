package com.sparta.errorpool.defaultResponse;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String CREATED_USER_FAILED = "회원 가입 실패";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String UPDATE_SKILL_SUCCESS = "업데이트 성공";
    public static final String UPDATE_SKILL_FAILED = "업데이트 실패";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";

    public static final String DUPLICATE_EMAIL = "중복된 이메일 입니다.";
    public static final String USERNAME_LENGTH = "닉네임은 3자 이상 12자 이하로 입력해 주세요";
    public static final String USERNAME_PATTERN = "특수문자는 사용하실 수 없습니다.";
    public static final String EMAILFORM_ERROR = "이메일 형식이 올바르지 않습니다.";
    public static final String PASSWORD_LENGTH = "비밀번호는 6자 이상 12자 이하로 입력해 주세요.";
    public static final String PASSWORD_CONTAINS_ID = "비밀번호에 아이디문자열이 포함될 수 없습니다.";
}
