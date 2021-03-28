package com.joker17.generator.freemarker.utils;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResourceType;
import com.joker17.generator.common.utils.GeneratorUtils;
import com.joker17.generator.common.support.GeneratorWorker;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.*;

public class FreemarkerUtils {

    private static volatile Map<String, Configuration> configurationCacheMap = new HashMap<>(16);

    public static Configuration getConfiguration(GeneratorResourceType resourceType, String templateGlobalPath) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(resourceType.getValue()).append(":").append(templateGlobalPath);
        String key = builder.toString();
        Configuration configuration = configurationCacheMap.get(key);
        if (configuration == null) {
            synchronized (FreemarkerUtils.class) {
                configuration = configurationCacheMap.get(key);
                if (configuration == null) {
                    configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
                    if (resourceType == GeneratorResourceType.FILE) {
                        configuration.setDirectoryForTemplateLoading(new File(templateGlobalPath));
                    } else if (resourceType == GeneratorResourceType.CLASSPATH) {
                        configuration.setClassForTemplateLoading(FreemarkerUtils.class, "/");
                    }
                    configurationCacheMap.put(key, configuration);
                }
            }
        }
        return configuration;
    }

    public static Map<String, ByteArrayOutputStream> generator(final GeneratorParam generatorParamModel) throws IOException {

        return GeneratorUtils.resolveGenerator(generatorParamModel, new GeneratorWorker<Template, Object>() {

            private Configuration configuration;

            @Override
            public void init(boolean hasTemplateGlobalPath, String templateGlobalPath, GeneratorResourceType resourceType) throws IOException {
                configuration = getConfiguration(resourceType, templateGlobalPath);
            }

            @Override
            public Template getTemplate(String templatePath, String charset) throws IOException {
                return configuration.getTemplate(templatePath, charset);
            }

            @Override
            public Object getWorkModel(Map<String, Object> dataConfig) {
                return dataConfig;
            }

            @Override
            public void process(Template template, Object workModel, Writer writer) throws IOException {
                processExecute(template, workModel, writer);
            }
        });

    }

    private static void processExecute(Template template, Object dataModel, Writer out) throws IOException {
        try {
            template.process(dataModel, out);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw e;
        }
    }

}
