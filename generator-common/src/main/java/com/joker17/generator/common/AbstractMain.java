package com.joker17.generator.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.support.ScannerSupport;
import com.joker17.generator.common.utils.YamlUtils;
import org.apache.commons.io.IOUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public abstract class AbstractMain {

    protected abstract void generator(GeneratorParam generatorParam) throws IOException;

    protected abstract String generatorBy();

    public void run() {
        String generatorBy = generatorBy();
        ScannerSupport scannerSupport = new ScannerSupport();

        while (true) {
            System.out.println(String.format("==========<[%s]读取配置文件渲染模板开始>==========", generatorBy));
            System.out.println("请选择配置文件格式: 1: yml, 2: json");
            long formatValue = scannerSupport.nextLongWhenTrue("输入值格式错误,请重新输入: ", Arrays.asList(1L, 2L), "文件格式值输入错误,请重新输入: ");
            System.out.println("请输入配置文件路径: ");

            //解析配置文件
            GeneratorParam generatorParam = null;
            FileInputStream configFileInputStream = null;
            try {
                String path = scannerSupport.next();

                if (formatValue == 1) {
                    if (path == null || !path.endsWith(".yml")) {
                        throw new IllegalArgumentException("非yml文件");
                    }
                    configFileInputStream = new FileInputStream(path);
                    generatorParam = YamlUtils.toJavaObject(configFileInputStream, GeneratorParam.class);
                } else {
                    if (path == null || !path.endsWith(".json")) {
                        throw new IllegalArgumentException("非json文件");
                    }
                    generatorParam = JSONObject.parseObject(IOUtils.toString(configFileInputStream, "UTF-8"), GeneratorParam.class);
                }

            } catch (FileNotFoundException e) {
                System.err.println("请输入正确的文件路径");
            } catch (Exception e) {
                String msg = e.getMessage();
                System.err.println(String.format("读取配置文件错误%s", msg != null ? ": " + msg : ""));
            } finally {
                //关闭流
                if (configFileInputStream != null) {
                    IOUtils.closeQuietly(configFileInputStream);
                }
            }

            try {
                boolean paramPrint = Boolean.parseBoolean(System.getProperty("generator.param.print", "false"));
                if (paramPrint) {
                    System.out.println(
                            "\n--------- generator param -------------\n"
                                    + JSONObject.toJSONString(
                                    generatorParam,
                                    SerializerFeature.PrettyFormat,
                                    SerializerFeature.WriteMapNullValue,
                                    SerializerFeature.WriteDateUseDateFormat
                            ) +
                                    "\n--------- generator param -------------\n"
                    );
                }
                generator(generatorParam);
                System.out.println("处理成功..");
            } catch (Exception e) {
                System.err.println("处理失败: " + e.getMessage());
            }

            System.out.println("是否退出(1: 是, 0: 否): ");
            long isQuitValue = scannerSupport.nextLongWithDefaultValue(0);
            if (isQuitValue == 1) {
                System.out.println(String.format("==========<[%s]读取配置文件渲染模板结束>==========", generatorBy));
                break;
            }
        }
    }

}
