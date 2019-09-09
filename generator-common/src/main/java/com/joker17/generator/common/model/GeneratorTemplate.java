package com.joker17.generator.common.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GeneratorTemplate {

    /**
     * 模板全局路径
     */
    private String path;


    /**
     * 模板配置 (模板项 -> 导出文件路径/byte)
     * e.g:
     *  default.html.vm -> byte  (添加到返回结果中)
     *  add.html.vm -> /template/${package}/add.html (写入文件中)
     *  /config/default.config.vm -> /config/${package}/default.config
     *  ~zip! -> byte/导出文件路径 (以zip方式导出)
     */
    private Map<String, String> config = new HashMap<>(16);


    /**
     * 数据配置 (变量值用于模板数据渲染及模板配置中导出文件路径的处理)
     *
     */
    private Map<String, Object> data = new HashMap<>(16);


}
