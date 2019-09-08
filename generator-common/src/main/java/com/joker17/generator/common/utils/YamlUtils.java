package com.joker17.generator.common.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlUtils {


    public static <T> T toJavaObject(InputStream inputStream, Class<T> type) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(inputStream, type);
    }

}
