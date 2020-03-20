package com.joker17.generator.test;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorTemplate;
import com.joker17.generator.common.service.GeneratorService;
import com.joker17.generator.freemarker.iml.FreemarkerGeneratorImpl;
import com.joker17.generator.velocity.engine.iml.VelocityEngineGeneratorImpl;
import com.joker17.generator.velocity.iml.VelocityGeneratorImpl;

import java.util.Map;

/**
 * 通过test文件运行时需要进行处理路径.
 * 单test运行测试.
 */
public class BaseTests {

    protected void generatorBefore(GeneratorParam generatorParam) {

    }


    protected void resolvePathAndOutDir(GeneratorParam generatorParam) {
        //处理输入路径
        GeneratorTemplate template = generatorParam.getTemplate();
        template.setPath(template.getPath().replace("generator-test/", ""));

        //处理输出路径
        Map<String, Object> data = template.getData();
        String outDir = (String) data.get("outDir");
        data.put("outDir", "../" + outDir);
    }


    protected void resolveOutDir(GeneratorParam generatorParam) {
        GeneratorTemplate template = generatorParam.getTemplate();

        //处理输出路径
        Map<String, Object> data = template.getData();
        String outDir = (String) data.get("outDir");
        data.put("outDir", "../" + outDir);
    }

}
