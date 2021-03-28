package com.joker17.generator.common.enums;

public enum GeneratorExportTypeEnum {

    BYTE,

    FILE {
        @Override
        public boolean isMatch(String text) {
            for (GeneratorExportTypeEnum value : GeneratorExportTypeEnum.values()) {
                if (!value.equals(this) && value.isMatch(text)) {
                    return false;
                }
            }
            return true;
        }
    },

    NONE;

    public boolean isMatch(String text) {
        return this.name().equalsIgnoreCase(text);
    }
}
