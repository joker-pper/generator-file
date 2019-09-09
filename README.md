# generator-file

*使用GeneratorParam参数统一进行渲染文件*

## 支持模板类型:
    freemarker velocity velocity-engine
    
## GeneratorParam参数:
```
    charset(String): 字符集,用于读取和输出文件
    type(String): 读取模板文件方式: file/classpath [GeneratorResoureType中value值]
    template(GeneratorTemplate): 模板资源配置
```
## GeneratorTemplate参数:
```
    path(String): 模版资源全局路径 (classpath时以根目录为首)
    config(Map<String, String>):
        模板配置 (模板项 -> 导出文件路径/byte)
        e.g:
            default.html.vm -> byte  (添加到返回结果中)
            add.html.vm -> /template/${package}/add.html (写入文件中)
            /config/default.config.vm -> /config/${package}/default.config
            ~zip! -> byte/导出文件路径 (以zip方式导出,可选)
    data(Map<String, Object>):    
        数据配置 (变量值用于模板数据渲染及模板配置中导出文件路径的处理)
```   
## api
``` 

//通过工具类
FreemarkerUtils.generator(generatorParamModel);
VelocityUtils.generator(generatorParamModel);
VelocityEngineUtils.generator(generatorParamModel);

//通过对象
GeneratorService freemarkerService = new FreemarkerGeneratorImpl();
freemarkerService.generator(generatorParamModel);

GeneratorService velocityService = new VelocityGeneratorImpl();
velocityService.generator(generatorParamModel);

GeneratorService velocityEngineService = new VelocityEngineGeneratorImpl();
velocityEngineService.generator(generatorParamModel);

``` 

## 其他     
### generator-file-all 提供打包为可执行jar并支持直接选择模板类型及配置文件(yml/json)进行生成文件
    - 可通过命令运行: java -Dfile.encoding=utf-8 -jar generator-file-all-1.0-SNAPSHOT.jar  
