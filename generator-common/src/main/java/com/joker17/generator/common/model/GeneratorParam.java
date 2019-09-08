package com.joker17.generator.common.model;

import lombok.Data;

@Data
public class GeneratorParam {

    /**
     * 字符集
     */
    private String charset = "UTF-8";

    /**
     * 读取模板资源方式: file/classpath [GeneratorResoureType中value值]
     */
    private String type;

    /**
     * 模板资源配置
     */
    private GeneratorTemplate template;

}
