package com.joker17.generator.test;

import com.joker17.generator.beetl.iml.BeetlGeneratorImpl;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.freemarker.iml.FreemarkerGeneratorImpl;
import com.joker17.generator.velocity.engine.iml.VelocityEngineGeneratorImpl;
import com.joker17.generator.velocity.iml.VelocityGeneratorImpl;

public enum TestCase {

    FREEMARKER("src/main/resources/template/freemarker/", new FreemarkerGeneratorImpl()),
    VELOCITY("src/main/resources/template/velocity/", new VelocityGeneratorImpl()),
    VELOCITY_ENGINE("src/main/resources/template/velocity-engine/", new VelocityEngineGeneratorImpl()),
    BEETL("src/main/resources/template/beetl/", new BeetlGeneratorImpl()),
    ;

    private final String dirPath;

    private final GeneratorService generatorService;

    TestCase(String dirPath, GeneratorService generatorService) {
        this.dirPath = dirPath;
        this.generatorService = generatorService;
    }

    public String getDirPath() {
        return dirPath;
    }


    public GeneratorService getGeneratorService() {
        return generatorService;
    }
}
