package com.joker17.generator.freemarker.utils;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResoureType;
import com.joker17.generator.common.model.GeneratorTemplate;
import com.joker17.generator.common.utils.GeneratorUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FreemarkerUtils {

    private static volatile Map<String, Configuration> configurationCacheMap = new HashMap<>(16);

    public static Configuration getConfiguration(GeneratorResoureType resoureType, String templateGlobalPath) throws IOException {
        templateGlobalPath = templateGlobalPath == null || templateGlobalPath.isEmpty() ? "" : templateGlobalPath;

        if (resoureType == GeneratorResoureType.FILE) {
            if (!templateGlobalPath.isEmpty() && !templateGlobalPath.endsWith("/")) {
                templateGlobalPath += "/";
            }
        } else if (resoureType == GeneratorResoureType.CLASSPATH) {
            //classpath时设置为根目录
            templateGlobalPath = "/";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(resoureType.getValue()).append(":").append(templateGlobalPath);
        String key = builder.toString();
        Configuration configuration = configurationCacheMap.get(key);
        if (configuration == null) {
            synchronized (FreemarkerUtils.class) {
                configuration = configurationCacheMap.get(key);
                if (configuration == null) {
                    configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
                    if (resoureType == GeneratorResoureType.FILE) {
                        configuration.setDirectoryForTemplateLoading(new File(templateGlobalPath));
                    } else if (resoureType == GeneratorResoureType.CLASSPATH) {
                        configuration.setClassForTemplateLoading(FreemarkerUtils.class, templateGlobalPath);
                    }
                    configurationCacheMap.put(key, configuration);
                }
            }
        }
        return configuration;
    }

    public static Map<String, ByteArrayOutputStream> generator(GeneratorParam generatorParamModel) throws IOException {
        String resoureTypeStr = generatorParamModel.getType();
        GeneratorResoureType resoureType = null;
        if (resoureTypeStr != null) {
            for (GeneratorResoureType current : GeneratorResoureType.values()) {
                if (current.getValue().equals(resoureTypeStr)) {
                    resoureType = current;
                    break;
                }
            }
        }

        Objects.requireNonNull(resoureType, "resoureType must be not null");
        GeneratorTemplate generatorTemplate = generatorParamModel.getTemplate();

        String templateGlobalPath = generatorTemplate.getPath();
        boolean hasTemplateGlobalPath = templateGlobalPath != null && templateGlobalPath.length() > 0;

        Configuration configuration = getConfiguration(resoureType, templateGlobalPath);

        //JSONObject dataConfig = (JSONObject) JSONObject.toJSON(generatorTemplate.getData());
        Map<String, Object> dataConfig = generatorTemplate.getData();

        //获取模板列表
        Map<String, String> templateConfigMap = generatorTemplate.getConfig();

        String zipPath = templateConfigMap.get(GeneratorUtils.ZIP_CONFIG);
        boolean isExportZip = zipPath != null && zipPath.length() > 0;

        Map<String, ByteArrayOutputStream> byteArrayOutputStreamMap = new HashMap<>();

        ZipOutputStream zipOutputStream = null;
        boolean isZipByte = isExportZip && zipPath.equals("byte");

        if (isExportZip) {
            templateConfigMap.remove(GeneratorUtils.ZIP_CONFIG);
            if (isZipByte) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
                byteArrayOutputStreamMap.put(GeneratorUtils.ZIP_CONFIG, byteArrayOutputStream);
            } else {
                zipPath = GeneratorUtils.parseVarExpressValue(zipPath, dataConfig);
                File currentFile = new File(zipPath);
                GeneratorUtils.resolveDirs(currentFile);
                zipOutputStream = new ZipOutputStream(new FileOutputStream(currentFile));
            }

        }

        String charset = generatorParamModel.getCharset();
        boolean hasIOException = false;
        try {
            for (String template : templateConfigMap.keySet()) {
                //获取模板资源
                String templatePath;
                if (resoureType == GeneratorResoureType.CLASSPATH) {
                    if (!hasTemplateGlobalPath) {
                        templatePath = template;
                    } else {
                        templatePath = GeneratorUtils.resolvePath(templateGlobalPath, template);
                    }
                } else {
                    templatePath = template;
                }

                Template tpl = configuration.getTemplate(templatePath, charset);
                OutputStreamWriter streamWriter = null;
                //处理导出文件路径
                String exportFilePath = templateConfigMap.get(template);
                boolean isByte = "byte".equals(exportFilePath);
                if (isByte) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    streamWriter = new OutputStreamWriter(byteArrayOutputStream, charset);

                    process(tpl, dataConfig, streamWriter);

                    byteArrayOutputStreamMap.put(template, byteArrayOutputStream);
                } else {
                    //获取路径值
                    exportFilePath = GeneratorUtils.parseVarExpressValue(exportFilePath, dataConfig);

                    if (isExportZip) {
                        //导出zip时
                        zipOutputStream.putNextEntry(new ZipEntry(exportFilePath));
                        streamWriter = new OutputStreamWriter(zipOutputStream, charset);
                        process(tpl, dataConfig, streamWriter);
                        zipOutputStream.closeEntry();
                        zipOutputStream.flush();
                    } else {
                        File exportFile = new File(exportFilePath);
                        GeneratorUtils.resolveDirs(exportFile);
                        FileOutputStream fileOutputStream = new FileOutputStream(exportFile);
                        streamWriter = new OutputStreamWriter(fileOutputStream, charset);
                        process(tpl, dataConfig, streamWriter);
                        IOUtils.closeQuietly(fileOutputStream);
                    }
                }

                IOUtils.closeQuietly(streamWriter);
            }
        } catch (IOException e) {
            hasIOException = true;
            throw e;
        } finally {
            if (isExportZip) {
                IOUtils.closeQuietly(zipOutputStream);
                if (!isZipByte && hasIOException) {
                    FileUtils.deleteQuietly(new File(zipPath));
                }
            }
        }
        return byteArrayOutputStreamMap;
    }

    private static void process(Template template, Object dataModel, Writer out) throws IOException {
        try {
            template.process(dataModel, out);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw e;
        }
    }

}
