package com.sparta.errorpool.article;

public enum Category {
    FREE_BOARD(1,"자유게시판"), ERROR(2,"오류공유"), TECH(3,"tech");

    private final Integer num;
    private final String name;

    private Category(Integer num, String name) {
        this.num = num;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getNum() {
        return num;
    }

    public static Category getCategoryById(Integer id) {
        if ( id.equals(1) ) {
            return FREE_BOARD;
        } else if ( id.equals(2) ) {
            return ERROR;
        } else if ( id.equals(3) ) {
            return TECH;
        } else {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
        }
    }
}
