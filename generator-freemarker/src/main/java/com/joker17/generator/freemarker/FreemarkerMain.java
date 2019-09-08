package com.joker17.generator.freemarker;

import com.joker17.generator.common.AbstractMain;
import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.freemarker.utils.FreemarkerUtils;

import java.io.IOException;

public class FreemarkerMain extends AbstractMain {

    @Override
    protected void generator(GeneratorParam generatorParam) throws IOException {
        FreemarkerUtils.generator(generatorParam);
    }

    @Override
    protected String generatorBy() {
        return "freemarker";
    }

    public static void main(String[] args) {
        new FreemarkerMain().run();
    }
}
