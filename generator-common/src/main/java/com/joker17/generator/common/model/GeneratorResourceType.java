package com.joker17.generator.common.model;

public enum GeneratorResourceType {

    FILE("file"),

    CLASSPATH("classpath");

    private String value;

    GeneratorResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
