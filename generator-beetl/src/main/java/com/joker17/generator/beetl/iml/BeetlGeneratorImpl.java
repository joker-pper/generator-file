package com.joker17.generator.beetl.iml;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.beetl.utils.BeetlUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class BeetlGeneratorImpl implements GeneratorService {

    @Override
    public Map<String, ByteArrayOutputStream> generator(GeneratorParam generatorParamModel) throws IOException {
        return BeetlUtils.generator(generatorParamModel);
    }

}
