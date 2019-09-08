package com.joker17.generator.velocity.utils;

import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResoureType;
import com.joker17.generator.common.model.GeneratorTemplate;
import com.joker17.generator.common.utils.GeneratorUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class VelocityUtils {


    private static volatile GeneratorResoureType INITED_GENERATOR_RESOUR_ETYPE = null;


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

        if (INITED_GENERATOR_RESOUR_ETYPE == null) {
            synchronized (VelocityUtils.class) {
                if (INITED_GENERATOR_RESOUR_ETYPE == null) {
                    INITED_GENERATOR_RESOUR_ETYPE = resoureType;
                }
            }
        }

        if (resoureType != INITED_GENERATOR_RESOUR_ETYPE) {
            throw new RuntimeException(String.format("velocity has load resource type [%s], not support current resource type [%s].", INITED_GENERATOR_RESOUR_ETYPE.getValue(), resoureType.getValue()));
        }

        // 设置velocity资源加载器
        Properties prop = new Properties();
        if (resoureType == GeneratorResoureType.FILE) {
            prop.put("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.FileResourceLoader.class.getName());
            if (hasTemplateGlobalPath) {
                prop.setProperty("file.resource.loader.path", templateGlobalPath);
            }
        } else if (resoureType == GeneratorResoureType.CLASSPATH) {
            prop.put("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader.class.getName());
        }
        Velocity.init(prop);


        //JSONObject dataConfig = (JSONObject) JSONObject.toJSON(generatorTemplate.getData());
        Map<String, Object>  dataConfig = generatorTemplate.getData();

        //封装模板数据
        VelocityContext context = new VelocityContext(dataConfig);

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
                //获取模板资源
                Template tpl = Velocity.getTemplate(templatePath, charset);
                StringWriter sw = new StringWriter();
                tpl.merge(context, sw);

                //处理导出文件路径
                String exportFilePath = templateConfigMap.get(template);

                boolean isByte = "byte".equals(exportFilePath);

                if (isByte) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    IOUtils.write(sw.toString(), byteArrayOutputStream, charset);
                    byteArrayOutputStreamMap.put(template, byteArrayOutputStream);
                } else {
                    //获取路径值
                    exportFilePath = GeneratorUtils.parseVarExpressValue(exportFilePath, dataConfig);

                    if (isExportZip) {
                        //导出zip时
                        zipOutputStream.putNextEntry(new ZipEntry(exportFilePath));
                        IOUtils.write(sw.toString(), zipOutputStream, charset);
                        zipOutputStream.closeEntry();
                        zipOutputStream.flush();
                    } else {
                        File exportFile = new File(exportFilePath);
                        GeneratorUtils.resolveDirs(exportFile);
                        FileOutputStream fileOutputStream = new FileOutputStream(exportFile);
                        IOUtils.write(sw.toString(), fileOutputStream, charset);
                        IOUtils.closeQuietly(fileOutputStream);
                    }
                }

                IOUtils.closeQuietly(sw);
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


}
