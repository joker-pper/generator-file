package com.joker17.generator.all;

import com.joker17.generator.common.support.ScannerSupport;

public class GeneratorMain {

    public void run() {
        ScannerSupport scannerSupport = new ScannerSupport();
        System.out.println("请选择使用模板类型: " + GeneratorCase.describe());
        int templateValue = (int) scannerSupport.nextLongWhenTrue("输入值格式错误,请重新输入: ", GeneratorCase.indexs(), "模板类型值输入错误,请重新输入: ");
        GeneratorCase.indexOf(templateValue).getMain().run();
    }

    public static void main(String[] args) {
        new GeneratorMain().run();
    }
}
