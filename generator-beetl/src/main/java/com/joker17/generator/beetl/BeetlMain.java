package com.joker17.generator.beetl;

import com.joker17.generator.common.AbstractMain;
import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.beetl.utils.BeetlUtils;

import java.io.IOException;

public class BeetlMain extends AbstractMain {

    @Override
    protected void generator(GeneratorParam generatorParam) throws IOException {
        BeetlUtils.generator(generatorParam);
    }

    @Override
    protected String generatorBy() {
        return "beetl";
    }

    public static void main(String[] args) {
        new BeetlMain().run();
    }
}
