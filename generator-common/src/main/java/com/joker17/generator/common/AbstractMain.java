package com.joker17.generator.common;

import com.alibaba.fastjson.JSONObject;
import com.joker17.generator.common.model.GeneratorParam;
import com.joker17.generator.common.utils.YamlUtils;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public abstract class AbstractMain {

    protected abstract void generator(GeneratorParam generatorParam) throws IOException;

    protected abstract String generatorBy();

    public void run() {

        String generatorBy = generatorBy();

        while (true) {
            System.out.println(String.format("==========<[%s]读取配置文件渲染模板开始>==========", generatorBy));
            System.out.println("请选择配置文件格式: 1: yml, 2: json");
            Scanner scanner = new Scanner(System.in);
            try {
                long format = scanner.nextLong();
                if (format != 1 && format != 2) {
                    throw new IllegalArgumentException();
                }
                System.out.println("请输入配置文件路径: ");

                GeneratorParam generatorParam;
                try {
                    String path = scanner.next();

                    if (format == 1) {
                        if (path == null || !path.endsWith(".yml")) {
                            throw new IllegalArgumentException("非yml文件");
                        }
                        generatorParam = YamlUtils.toJavaObject(new FileInputStream(path), GeneratorParam.class);
                    } else {
                        if (path == null || !path.endsWith(".json")) {
                            throw new IllegalArgumentException("非json文件");
                        }
                        generatorParam = JSONObject.parseObject(IOUtils.toString(new FileInputStream(path), "UTF-8"), GeneratorParam.class);
                    }

                    try {
                        generator(generatorParam);
                        System.out.println("<--- 处理成功 --->");
                    } catch (IOException e) {
                        System.err.println("处理失败: " + e.getMessage());

                    }

                } catch (FileNotFoundException e) {
                    System.err.println("请输入正确的文件路径");

                } catch (Exception e) {
                    String msg = e.getMessage();
                    System.err.println(String.format("读取配置文件错误%s", msg != null ? ": " + msg : ""));
                }

            } catch (Exception e) {
                String msg = e.getMessage();
                msg = msg == null ? "文件格式值输入错误" : msg;
                System.err.println(msg);

            }

            System.out.println("是否退出(1: 是, 0: 否): ");
            long value = scanner.nextLong();

            if (value == 1) {
                System.out.println("退出...");
            }

            System.out.println(String.format("==========<[%s]读取配置文件渲染模板结束>==========", generatorBy));

            if (value == 1) {
                break;
            }
        }

    }


}
