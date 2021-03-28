package com.joker17.generator.common.utils;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlUtils {


    public static <T> T toJavaObject(InputStream inputStream, Class<T> type) {
        try {
            Yaml yaml = new Yaml();
            return yaml.loadAs(inputStream, type);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

}
