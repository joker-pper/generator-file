package com.joker17.generator.common.model;

import com.joker17.generator.common.utils.GeneratorUtils;
import lombok.Data;

@Data
public class GeneratorParam {

    /**
     * 字符集: 默认输入输出一致 多个时以,分隔 [0]输入 [1]输出
     *
     */
    private String charset = GeneratorUtils.UTF8_TEXT;


    /**
     * 读取模板资源方式: file/classpath [GeneratorResourceType中value值]
     */
    private String type;

    /**
     * 模板资源配置
     */
    private GeneratorTemplate template;

}
