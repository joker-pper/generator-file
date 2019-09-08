package com.joker17.generator.common.utils;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneratorUtils {

    public static final String ZIP_CONFIG = "~zip!";


    /**
     * 获取变量表达式
     *
     * @param source
     * @return
     */
    public static List<String> getVarExpressList(String source) {
        if (source != null && source.length() > 0) {
            String regex = "\\$\\{.+?\\}";
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
     * @param source
     * @param dataConfig
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
     * 处理文件文件夹
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
}
