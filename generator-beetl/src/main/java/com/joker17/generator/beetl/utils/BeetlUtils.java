package com.joker17.generator.beetl.utils;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResourceType;
import com.joker17.generator.common.utils.GeneratorUtils;
import com.joker17.generator.common.support.GeneratorWorker;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.FileResourceLoader;

import java.io.*;
import java.util.*;

public class BeetlUtils {

    private volatile static Configuration configurationCache = null;

    public static Map<String, ByteArrayOutputStream> generator(final GeneratorParam generatorParamModel) throws IOException {

        return GeneratorUtils.resolveGenerator(generatorParamModel, new GeneratorWorker<Template, Map<String, Object>>() {

            private GroupTemplate groupTemplate;

            @Override
            public void init(boolean hasTemplateGlobalPath, String templateGlobalPath, GeneratorResourceType resourceType) throws IOException {
                if (configurationCache == null) {
                    synchronized (BeetlUtils.class) {
                        if (configurationCache == null) {
                            configurationCache = Configuration.defaultConfiguration();
                        }
                    }
                }

                if (resourceType == GeneratorResourceType.CLASSPATH) {
                    groupTemplate = new GroupTemplate(new ClasspathResourceLoader("/"), configurationCache);
                } else if (resourceType == GeneratorResourceType.FILE) {
                    groupTemplate = new GroupTemplate(new FileResourceLoader(templateGlobalPath), configurationCache);
                }

            }

            @Override
            public Template getTemplate(String templatePath, String charset) throws IOException {
                return groupTemplate.getTemplate(templatePath);
            }

            @Override
            public Map<String, Object> getWorkModel(Map<String, Object> dataConfig) {
                return dataConfig;
            }

            @Override
            public void process(Template template, Map<String, Object> workModel, Writer writer) throws IOException {
                template.binding(workModel);
                template.renderTo(writer);
            }
        });

    }



}
