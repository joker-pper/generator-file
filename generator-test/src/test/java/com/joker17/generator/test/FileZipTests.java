package com.joker17.generator.test;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResourceType;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.common.utils.YamlUtils;
import org.junit.Test;

import java.io.FileInputStream;

public class FileZipTests extends BaseTests {

    private final static String FILE_NAME = "config-file-zip.yml";


    @Test
    public void test() throws Exception {

        for (TestCase testCase : TestCase.values()) {
            if (!testCase.isSupport(GeneratorResourceType.FILE)) {
                continue;
            }

            GeneratorService generatorService = testCase.getGeneratorService();
            GeneratorParam generatorParam = YamlUtils.toJavaObject(new FileInputStream(testCase.getDirPath() + FILE_NAME), GeneratorParam.class);

            resolvePathAndOutDir(generatorParam);

            generatorBefore(generatorParam);

            generatorService.generator(generatorParam);
        }
    }

}
