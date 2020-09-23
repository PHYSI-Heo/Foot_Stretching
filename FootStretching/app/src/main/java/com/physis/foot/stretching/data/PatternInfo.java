package com.physis.foot.stretching.data;

public class PatternInfo {

    private String code, name, keyword;

    public PatternInfo(String code, String name, String keyword){
        this.code = code;
        this.name = name;
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getKeyword() {
        return keyword;
    }
}
