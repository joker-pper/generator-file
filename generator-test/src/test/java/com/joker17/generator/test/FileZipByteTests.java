package com.joker17.generator.test;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResourceType;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.common.utils.GeneratorUtils;
import com.joker17.generator.common.utils.YamlUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

public class FileZipByteTests extends BaseTests {

    private final static String FILE_NAME = "config-file-zip-byte.yml";

    @Test
    public void test() throws Exception {

        for (TestCase testCase : TestCase.values()) {
            if (!testCase.isSupport(GeneratorResourceType.FILE)) {
                continue;
            }

            GeneratorService generatorService = testCase.getGeneratorService();
            GeneratorParam generatorParam = YamlUtils.toJavaObject(new FileInputStream(testCase.getDirPath() + FILE_NAME), GeneratorParam.class);

            resolvePathAndOutDir(generatorParam);

            //处理输出路径
            File outDirFile = new File((String) generatorParam.getTemplate().getData().get("outDir"));
            File outZipFile = new File(outDirFile, "zip-byte.zip");
            GeneratorUtils.resolveDirs(outZipFile);

            generatorBefore(generatorParam);

            Map<String, ByteArrayOutputStream> byteArrayOutputStreamMap = generatorService.generator(generatorParam);
            try (ByteArrayOutputStream byteArrayOutputStream = byteArrayOutputStreamMap.get(GeneratorUtils.ZIP_CONFIG_KEY)) {
                FileOutputStream fileOutputStream = new FileOutputStream(outZipFile);
                try {
                    IOUtils.write(byteArrayOutputStream.toByteArray(), fileOutputStream);
                } finally {
                    IOUtils.closeQuietly(fileOutputStream);
                }
                Assert.assertTrue(String.format("%s 生成文件失败", testCase.name()), outZipFile.exists() && outZipFile.isFile() && outZipFile.length() > 0);
            }
        }
    }

}
