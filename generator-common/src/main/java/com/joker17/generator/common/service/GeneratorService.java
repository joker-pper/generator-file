package com.joker17.generator.common.service;

import com.joker17.generator.common.model.GeneratorParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public interface GeneratorService {

    Map<String, ByteArrayOutputStream> generator(GeneratorParam generatorParamModel) throws IOException;
}
