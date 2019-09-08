package com.joker17.generator.velocity.iml;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.velocity.utils.VelocityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class VelocityGeneratorImpl implements GeneratorService {

    @Override
    public Map<String, ByteArrayOutputStream> generator(GeneratorParam generatorParamModel) throws IOException {
        return VelocityUtils.generator(generatorParamModel);
    }

}
