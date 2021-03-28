package com.joker17.generator.velocity.engine.utils;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResourceType;
import com.joker17.generator.common.utils.GeneratorUtils;
import com.joker17.generator.common.support.GeneratorWorker;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.*;

public class VelocityEngineUtils {

    public static Map<String, ByteArrayOutputStream> generator(final GeneratorParam generatorParamModel) throws IOException {

        return GeneratorUtils.resolveGenerator(generatorParamModel, new GeneratorWorker<Template, VelocityContext>() {

            private VelocityEngine velocityEngine;

            @Override
            public void init(boolean hasTemplateGlobalPath, String templateGlobalPath, GeneratorResourceType resourceType) {
                // 设置velocity资源加载器
                Properties prop = new Properties();

                if (resourceType == GeneratorResourceType.FILE) {
                    prop.put("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.FileResourceLoader.class.getName());
                    if (hasTemplateGlobalPath) {
                        prop.setProperty("file.resource.loader.path", templateGlobalPath);
                    }
                } else if (resourceType == GeneratorResourceType.CLASSPATH) {
                    prop.put("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader.class.getName());
                }

                velocityEngine = new VelocityEngine(prop);
                //Velocity.init(prop);
            }

            @Override
            public Template getTemplate(String templatePath, String charset) {
                return velocityEngine.getTemplate(templatePath, charset);
                //return Velocity.getTemplate(templatePath, charset);
            }

            @Override
            public VelocityContext getWorkModel(Map<String, Object> dataConfig) {
                return new VelocityContext(dataConfig);
            }

            @Override
            public void process(Template template, VelocityContext workModel, Writer writer) {
                template.merge(workModel, writer);
            }
        });

    }


}
