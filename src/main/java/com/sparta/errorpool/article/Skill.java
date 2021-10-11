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
}
