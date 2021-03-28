package com.joker17.generator.common.utils;

import com.joker17.generator.common.enums.GeneratorExportTypeEnum;
import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.model.GeneratorResourceType;
import com.joker17.generator.common.model.GeneratorTemplate;
import com.joker17.generator.common.support.GeneratorWorker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeneratorUtils {

    public static final String ZIP_CONFIG_KEY = "~zip!";

    public static final String UTF8_TEXT = "UTF-8";

    public static final int BUFFER_SIZE = 4096;


    /**
     * 获取变量表达式
     *
     * @param source
     * @return
     */
    public static List<String> getVarExpressList(String source) {
        if (source != null && source.length() > 0) {
            String regex = "\\$\\{(.*?)\\}";
            Pattern r = Pattern.compile(regex);
            Matcher m = r.matcher(source);
            List<String> results = new ArrayList<>(16);
            while (m.find()) {
                String matchText = m.group();
                results.add(matchText);
            }
            return results;
        }
        return Collections.emptyList();
    }


    /**
     * 获取转换后的value值
     *
     * @param source     源文本值
     * @param dataConfig 配置数据
     * @return
     */
    public static String parseVarExpressValue(String source, Map<String, Object> dataConfig) {
        List<String> varExpressList = getVarExpressList(source);
        if (!varExpressList.isEmpty()) {
            //存在表达式时
            for (String varExpress : varExpressList) {
                //获取变量名称
                String varName = varExpress.substring(2, varExpress.length() - 1).trim();
                //获取对应的值
                Object varExpressValue = dataConfig.get(varName);
                Objects.requireNonNull(varExpressValue, String.format("data config not found var %s value", varName));

                String varExpressStrValue = varExpressValue.toString();

                //替换变量值
                source = source.replace(varExpress, varExpressStrValue);
            }
        }
        return source;
    }


    /**
     * 处理当前数据配置的值(当文本值包含变量表达式时进行替换处理)
     *
     * @param dataConfig
     */
    public static void resolveWithVarExpressValue(Map<String, Object> dataConfig) {
        for (Map.Entry<String, Object> entry : dataConfig.entrySet()) {
            Object value = entry.getValue();
            if (value == null || !(value instanceof String)) {
                continue;
            }
            String valueStr = (String) value;
            if (!valueStr.isEmpty()) {
                //处理当前数据配置的值
                entry.setValue(parseVarExpressValue(valueStr, dataConfig));
            }
        }
    }

    /**
     * 获取资源类型
     *
     * @param resourceTypeStr
     * @return
     */
    public static GeneratorResourceType getGeneratorResourceType(String resourceTypeStr) {
        GeneratorResourceType resourceType = null;
        if (resourceTypeStr != null) {
            for (GeneratorResourceType current : GeneratorResourceType.values()) {
                if (current.getValue().equals(resourceTypeStr)) {
                    resourceType = current;
                    break;
                }
            }
        }
        Objects.requireNonNull(resourceType, "resourceType must be not null");
        return resourceType;
    }

    /**
     * 获取数据配置
     *
     * @param generatorTemplate
     * @return
     */
    public static Map<String, Object> getDataConfig(GeneratorTemplate generatorTemplate) {
        Map<String, Object> dataConfig = generatorTemplate.getData();
        if (Boolean.TRUE.equals(generatorTemplate.getBoost())) {
            resolveWithVarExpressValue(dataConfig);
        }
        return dataConfig;
    }


    /**
     * 处理文件文件夹
     *
     * @param file
     */
    public static void resolveDirs(File file) {
        if (file != null) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.isDirectory()) {
                //父类非目录时进行创建
                parentFile.mkdirs();
            }
        }

    }


    public static String resolvePath(String dir, String path) {
        String result;
        if (dir == null || dir.isEmpty()) {
            result = path;
        } else {
            if (dir.endsWith("/") && path.startsWith("/")) {
                result = dir + path.substring(1);
            } else if (dir.endsWith("/") || path.startsWith("/")) {
                result = dir + path;
            } else {
                result = dir + "/" + path;
            }

        }
        return result;
    }


    /**
     * 获取模板文件路径
     * @param hasTemplateGlobalPath
     * @param resourceType
     * @param templateGlobalPath
     * @param template
     * @return
     */
    public static String getTemplatePath(boolean hasTemplateGlobalPath, GeneratorResourceType resourceType, String templateGlobalPath, String template) {
        String templatePath;
        if (resourceType == GeneratorResourceType.CLASSPATH) {
            if (!hasTemplateGlobalPath) {
                templatePath = template;
            } else {
                templatePath = resolvePath(templateGlobalPath, template);
            }
        } else {
            templatePath = template;
        }
        return templatePath;
    }


    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String trimToNull(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty() ? null : value.trim();
    }

    /**
     * 获取字符集数组
     * @param charset
     * @return
     */
    public static String[] getCharsets(String charset) {
        if (charset == null || charset.isEmpty()) {
            charset = UTF8_TEXT;
        }

        String[] splitArray = charset.split(",", -1);

        String inputCharset;
        String outCharset = null;

        if (splitArray.length == 1) {
            inputCharset = getValueOrDefault(trimToNull(splitArray[0]), UTF8_TEXT);
        } else {
            inputCharset = getValueOrDefault(trimToNull(splitArray[0]), UTF8_TEXT);
            outCharset = trimToNull(splitArray[1]);
        }
        outCharset = getValueOrDefault(outCharset, inputCharset);
        return new String[] {inputCharset, outCharset};
    }


    /**
     * 进行输出文件的逻辑实现
     * @param generatorParamModel
     * @param worker
     * @param <Template>
     * @param <Model>
     * @return
     * @throws IOException
     */
    public static <Template, Model> Map<String, ByteArrayOutputStream> resolveGenerator(final GeneratorParam generatorParamModel, final GeneratorWorker<Template, Model> worker) throws IOException {

        GeneratorResourceType resourceType = getGeneratorResourceType(generatorParamModel.getType());

        GeneratorTemplate generatorTemplate = generatorParamModel.getTemplate();
        String templateGlobalPath = getValueOrDefault(trimToNull(generatorTemplate.getPath()), "");
        boolean hasTemplateGlobalPath = templateGlobalPath != null && templateGlobalPath.length() > 0;

        //初始化引擎
        worker.init(hasTemplateGlobalPath, templateGlobalPath, resourceType);

        //获取数据配置
        Map<String, Object> dataConfig = getDataConfig(generatorTemplate);

        //获取模板列表
        Map<String, String> templateConfigMap = generatorTemplate.getConfig();

        Map<String, ByteArrayOutputStream> byteArrayOutputStreamMap = new HashMap<>(16);

        //获取zip路径
        String zipPath = templateConfigMap.get(ZIP_CONFIG_KEY);

        boolean isExportZip = zipPath != null && zipPath.length() > 0;
        boolean isZipByte = isExportZip && GeneratorExportTypeEnum.BYTE.isMatch(zipPath);

        ZipOutputStream zipOutputStream = null;
        if (isExportZip) {
            //为导出zip时,移除模板中当前zip的额外配置
            templateConfigMap.remove(ZIP_CONFIG_KEY);
            if (isZipByte) {
                //为字节时
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
                zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
                byteArrayOutputStreamMap.put(ZIP_CONFIG_KEY, byteArrayOutputStream);
            } else {
                //为文件时
                zipPath = parseVarExpressValue(zipPath, dataConfig);
                File zipFile = new File(zipPath);
                resolveDirs(zipFile);
                zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            }
        }

        String[] charsets = getCharsets(generatorParamModel.getCharset());
        String inputCharset = charsets[0];
        String outCharset = charsets[1];

        //获取实际所需要的数据对象
        Model workModel = worker.getWorkModel(dataConfig);

        OutputStreamWriter streamWriter = null;

        boolean hasIOException = false;
        try {
            for (Map.Entry<String, String> entry : templateConfigMap.entrySet()) {
                String templateKey = entry.getKey();

                //处理导出文件路径
                String exportFilePath = entry.getValue();

                //获取模板资源
                String templatePath = getTemplatePath(hasTemplateGlobalPath, resourceType, templateGlobalPath, templateKey);

                //获取模板对象
                Template template = worker.getTemplate(templatePath, inputCharset);

                //处理模板文件

                if (GeneratorExportTypeEnum.BYTE.isMatch(exportFilePath) || GeneratorExportTypeEnum.NONE.isMatch(exportFilePath)) {

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
                    streamWriter = new OutputStreamWriter(byteArrayOutputStream, outCharset);

                    worker.process(template, workModel, streamWriter);
                    streamWriter.flush();

                    if (GeneratorExportTypeEnum.BYTE.isMatch(exportFilePath)) {
                        //为byte时设置
                        byteArrayOutputStreamMap.put(templateKey, byteArrayOutputStream);
                    }

                } else {
                    //获取导出的具体路径值
                    exportFilePath = parseVarExpressValue(exportFilePath, dataConfig);

                    if (isExportZip) {
                        //当导出为zip时写入zip中

                        zipOutputStream.putNextEntry(new ZipEntry(exportFilePath));
                        streamWriter = new OutputStreamWriter(zipOutputStream, outCharset);

                        worker.process(template, workModel, streamWriter);
                        streamWriter.flush();

                        zipOutputStream.closeEntry();
                        zipOutputStream.flush();
                    } else {
                        //当导出为文件时
                        File exportFile = new File(exportFilePath);
                        resolveDirs(exportFile);
                        FileOutputStream fileOutputStream = null;
                        try {
                            fileOutputStream = new FileOutputStream(exportFile);
                            streamWriter = new OutputStreamWriter(fileOutputStream, outCharset);
                            worker.process(template, workModel, streamWriter);
                            streamWriter.flush();
                        } finally {
                            IOUtils.closeQuietly(fileOutputStream);
                        }
                    }
                }
            }

        } catch (IOException e) {
            hasIOException = true;
            throw e;
        } finally {
            if (isExportZip) {
                IOUtils.closeQuietly(zipOutputStream);
                if (!isZipByte && hasIOException) {
                    //删除失败的zip文件
                    FileUtils.deleteQuietly(new File(zipPath));
                }
            }
            IOUtils.closeQuietly(streamWriter);
        }
        return byteArrayOutputStreamMap;
    }


}
