package com.sparta.errorpool.article;

public enum Category {
    TECH("tech"), FREE_BOARD("자유게시판"), ERROR("오류공유");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
