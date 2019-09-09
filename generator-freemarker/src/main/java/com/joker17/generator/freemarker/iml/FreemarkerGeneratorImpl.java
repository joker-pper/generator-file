package com.joker17.generator.freemarker.iml;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.freemarker.utils.FreemarkerUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class FreemarkerGeneratorImpl implements GeneratorService {

    @Override
    public Map<String, ByteArrayOutputStream> generator(GeneratorParam generatorParamModel) throws IOException {
        return FreemarkerUtils.generator(generatorParamModel);
    }

}
