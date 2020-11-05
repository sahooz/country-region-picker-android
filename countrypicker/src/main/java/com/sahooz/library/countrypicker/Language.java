package com.sahooz.library.countrypicker;

public enum Language {
    SIMPLIFIED_CHINESE("sc"), TRADITIONAL_CHINESE("tc"), ENGLISH("en");

    public final String key;
    private Language(String key) {
        this.key = key;
    }
}
