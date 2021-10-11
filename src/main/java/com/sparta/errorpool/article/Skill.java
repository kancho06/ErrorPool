package com.sparta.errorpool.article;

public enum Skill {
    REACT(1,"react"), SPRING(2,"spring"), NODEJS(3,"node.js");

    private final Integer num;
    private final String name;

    private Skill(Integer num, String name) {
        this.num = num;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Integer getNum() {
        return num;
    }

    public static Skill getSkillById(Integer id) {
        if ( id.equals(1) ) {
            return REACT;
        } else if ( id.equals(2) ) {
            return SPRING;
        } else if ( id.equals(3) ) {
            return NODEJS;
        }
        else {
            //todo 예외 만들어서 변경하기
            throw new RuntimeException();
        }
    }
}
