package com.sparta.errorpool.article;

public enum Category {
    TECH(1,"tech"), FREE_BOARD(2,"자유게시판"), ERROR(3,"오류공유");

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
            return TECH;
        } else if ( id.equals(2) ) {
            return FREE_BOARD;
        } else if ( id.equals(3) ) {
            return ERROR;
        } else {
            //todo 예외 만들어서 변경하기
            throw new RuntimeException();
        }
    }
}
