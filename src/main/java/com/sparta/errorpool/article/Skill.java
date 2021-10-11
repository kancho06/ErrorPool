package com.sparta.errorpool.article;

public enum Skill {
    REACT("react"), SPRING("spring"), NODEJS("node.js");

    private final String name;

    Skill(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
