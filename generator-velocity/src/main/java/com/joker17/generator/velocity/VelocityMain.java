package com.joker17.generator.velocity;

import com.joker17.generator.common.AbstractMain;
import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.velocity.utils.VelocityUtils;

import java.io.IOException;

public class VelocityMain extends AbstractMain {

    @Override
    protected void generator(GeneratorParam generatorParam) throws IOException {
        VelocityUtils.generator(generatorParam);
    }

    @Override
    protected String generatorBy() {
        return "velocity";
    }

    public static void main(String[] args) {
        new VelocityMain().run();
    }
}
