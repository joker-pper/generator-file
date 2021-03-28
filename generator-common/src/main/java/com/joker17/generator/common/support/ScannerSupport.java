package com.joker17.generator.common.support;

import java.util.List;
import java.util.Scanner;

public class ScannerSupport {

    private Scanner scanner = new Scanner(System.in);

    public long nextLongWhenTrue(String notTrueFormatText) {
        try {
            return scanner.nextLong();
        } catch (Exception e) {
            System.out.println(notTrueFormatText);
            scanner = new Scanner(System.in);
            return nextLongWhenTrue(notTrueFormatText);
        }
    }

    public long nextLongWhenTrue(String notTrueFormatText, List<Long> allowValueList, String notAllowValueText) {
        long value = nextLongWhenTrue(notTrueFormatText);
        if (allowValueList.contains(value)) {
            return value;
        }
        System.err.println(notAllowValueText);
        return nextLongWhenTrue(notTrueFormatText, allowValueList, notAllowValueText);
    }

    /**
     * 输入成功时返回输入值,反之返回默认值
     * @param defaultValue
     * @return
     */
    public long nextLongWithDefaultValue(long defaultValue) {
        try {
            return scanner.nextLong();
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public String next() {
        return scanner.next();
    }

}
