package com.joker17.generator.all;

import com.joker17.generator.beetl.BeetlMain;
import com.joker17.generator.common.AbstractMain;
import com.joker17.generator.freemarker.FreemarkerMain;
import com.joker17.generator.velocity.VelocityMain;
import com.joker17.generator.velocity.engine.VelocityEngineMain;

import java.util.ArrayList;
import java.util.List;

public enum GeneratorCase {

    FREEMARKER("freemarker", new FreemarkerMain()),
    VELOCITY("velocity", new VelocityMain()),
    VELOCITY_ENGINE("velocity-engine", new VelocityEngineMain()),
    BEETL("beetl", new BeetlMain()),
    ;

    private final String name;

    private final AbstractMain main;

    GeneratorCase(String name, AbstractMain main) {
        this.name = name;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public AbstractMain getMain() {
        return main;
    }

    public static String describe() {
        StringBuilder builder = new StringBuilder();
        GeneratorCase[] values = GeneratorCase.values();

        int length = values.length;
        int i = 0;
        for (GeneratorCase value : values) {
            builder.append(value.ordinal() + 1)
                    .append(": ")
                    .append(value.getName());
            if (i ++ != length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }



    public static List<Long> indexs() {
        GeneratorCase[] values = GeneratorCase.values();
        List<Long> indexs = new ArrayList<>(values.length);
        for (GeneratorCase value : values) {
            indexs.add((long) value.ordinal() + 1);
        }
        return indexs;
    }

    public static GeneratorCase indexOf(int index) {
        GeneratorCase[] values = GeneratorCase.values();
        if (index > values.length) {
            throw new IllegalArgumentException("max index: " + values.length);
        }
        GeneratorCase generatorCase = values[index - 1];
        if (generatorCase.ordinal() != index - 1) {
            for (GeneratorCase value : GeneratorCase.values()) {
                if (value.ordinal() == index - 1) {
                    generatorCase = value;
                    break;
                }

            }
        }

        return generatorCase;
    }



}
