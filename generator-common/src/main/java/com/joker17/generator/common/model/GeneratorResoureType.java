package com.joker17.generator.common.model;

public enum GeneratorResoureType {

    FILE("file"),

    CLASSPATH("classpath");

    private String value;

    GeneratorResoureType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
