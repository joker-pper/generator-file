package com.joker17.generator.velocity.engine;

import com.joker17.generator.common.AbstractMain;
import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.velocity.engine.utils.VelocityEngineUtils;

import java.io.IOException;

public class VelocityEngineMain extends AbstractMain {

    @Override
    protected void generator(GeneratorParam generatorParam) throws IOException {
        VelocityEngineUtils.generator(generatorParam);
    }

    @Override
    protected String generatorBy() {
        return "velocity-engine";
    }

    public static void main(String[] args) {
        new VelocityEngineMain().run();
    }
}
