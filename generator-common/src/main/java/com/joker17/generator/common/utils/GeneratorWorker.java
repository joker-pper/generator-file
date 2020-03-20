package com.joker17.generator.common.utils;

import com.joker17.generator.common.model.GeneratorResourceType;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface GeneratorWorker<Template, Model> {

    /**
     * 初始化
     * @param hasTemplateGlobalPath 是否存在全局路径
     * @param templateGlobalPath  全局路径
     * @param resourceType
     * @throws IOException
     */
    void init(boolean hasTemplateGlobalPath, String templateGlobalPath, GeneratorResourceType resourceType) throws IOException;

    /**
     * 获取数据对象
     * @param dataConfig
     * @return
     */
    Model getWorkModel(Map<String, Object> dataConfig);

    /**
     * 获取Template对象
     * @param templatePath
     * @param charset
     * @return
     * @throws IOException
     */
    Template getTemplate(String templatePath, String charset) throws IOException;

    /**
     * 处理模板数据
     * @param template
     * @param workModel
     * @param writer
     * @throws IOException
     */
    void process(Template template, Model workModel, Writer writer) throws IOException;
}
