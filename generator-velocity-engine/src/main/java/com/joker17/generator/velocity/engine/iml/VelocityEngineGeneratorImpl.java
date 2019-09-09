package com.joker17.generator.velocity.engine.iml;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.velocity.engine.utils.VelocityEngineUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class VelocityEngineGeneratorImpl implements GeneratorService {

    @Override
    public Map<String, ByteArrayOutputStream> generator(GeneratorParam generatorParamModel) throws IOException {
        return VelocityEngineUtils.generator(generatorParamModel);
    }

}
