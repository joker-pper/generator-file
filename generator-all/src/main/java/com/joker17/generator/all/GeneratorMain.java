package com.joker17.generator.all;

import com.joker17.generator.common.utils.ScannerSupport;
import com.joker17.generator.freemarker.FreemarkerMain;
import com.joker17.generator.velocity.VelocityMain;
import com.joker17.generator.velocity.engine.VelocityEngineMain;

import java.util.Arrays;

public class GeneratorMain {

    public void run() {
        ScannerSupport scannerSupport = new ScannerSupport();
        System.out.println("请选择使用模板类型: 1: freemarker, 2: velocity, 3:velocity-engine");
        int templateValue = (int) scannerSupport.nextLongWhenTrue("输入值格式错误,请重新输入: ", Arrays.asList(1L, 2L, 3L), "模板类型值输入错误,请重新输入: ");
        switch (templateValue) {
            case 1:
                new FreemarkerMain().run();
                break;
            case 2:
                new VelocityMain().run();
                break;
            case 3:
                new VelocityEngineMain().run();
                break;
        }
    }

    public static void main(String[] args) {
        new GeneratorMain().run();
    }
}
