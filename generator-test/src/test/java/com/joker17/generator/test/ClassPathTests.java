package com.joker17.generator.test;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.common.utils.YamlUtils;
import org.junit.Test;

import java.io.FileInputStream;

public class ClassPathTests extends BaseTests {

    private final static String FILE_NAME = "config-classpath.yml";


    @Test
    public void test() throws Exception {

        for (TestCase testCase : TestCase.values()) {
            GeneratorService generatorService = testCase.getGeneratorService();
            GeneratorParam generatorParam = YamlUtils.toJavaObject(new FileInputStream(testCase.getDirPath() + FILE_NAME), GeneratorParam.class);

            //处理输出路径
            resolveOutDir(generatorParam);

            generatorBefore(generatorParam);

            generatorService.generator(generatorParam);
        }
    }

}
