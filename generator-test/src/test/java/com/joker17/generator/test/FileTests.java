package com.joker17.generator.test;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.common.utils.YamlUtils;
import org.junit.Test;

import java.io.FileInputStream;

public class FileTests extends BaseTests {

    private final static String FILE_NAME = "config-file.yml";


    @Test
    public void test() throws Exception {

        for (TestCase testCase : TestCase.values()) {
            GeneratorService generatorService = testCase.getGeneratorService();
            GeneratorParam generatorParam = YamlUtils.toJavaObject(new FileInputStream(testCase.getDirPath() + FILE_NAME), GeneratorParam.class);
            resolvePathAndOutDir(generatorParam);

            generatorBefore(generatorParam);

            generatorService.generator(generatorParam);
        }
    }

}
