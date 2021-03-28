package com.joker17.generator.velocity.utils;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResourceType;
import com.joker17.generator.common.utils.GeneratorUtils;
import com.joker17.generator.common.support.GeneratorWorker;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.*;

public class VelocityUtils {

    private static volatile GeneratorResourceType INIT_GENERATOR_RESOURCE_TYPE = null;

    public static Map<String, ByteArrayOutputStream> generator(final GeneratorParam generatorParamModel) throws IOException {

        return GeneratorUtils.resolveGenerator(generatorParamModel, new GeneratorWorker<Template, VelocityContext>() {

            @Override
            public void init(boolean hasTemplateGlobalPath, String templateGlobalPath, GeneratorResourceType resourceType) {
                if (INIT_GENERATOR_RESOURCE_TYPE == null) {
                    synchronized (VelocityUtils.class) {
                        if (INIT_GENERATOR_RESOURCE_TYPE == null) {
                            INIT_GENERATOR_RESOURCE_TYPE = resourceType;
                        }
                    }
                }

                if (resourceType != INIT_GENERATOR_RESOURCE_TYPE) {
                    throw new RuntimeException(String.format("velocity has load resource type [%s], not support current resource type [%s].", INIT_GENERATOR_RESOURCE_TYPE.getValue(), resourceType.getValue()));
                }

                //设置velocity资源加载器
                Properties prop = new Properties();
                if (resourceType == GeneratorResourceType.FILE) {
                    prop.put("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.FileResourceLoader.class.getName());
                    if (hasTemplateGlobalPath) {
                        prop.setProperty("file.resource.loader.path", templateGlobalPath);
                    }
                } else if (resourceType == GeneratorResourceType.CLASSPATH) {
                    prop.put("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader.class.getName());
                }
                Velocity.init(prop);
            }

            @Override
            public Template getTemplate(String templatePath, String charset) {
                return Velocity.getTemplate(templatePath, charset);
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
