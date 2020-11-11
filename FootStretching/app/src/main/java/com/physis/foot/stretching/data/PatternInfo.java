package com.physis.foot.stretching.data;

public class PatternInfo {

    private String code, name, keyword, explanation;
    private String maker, runtime;

    public PatternInfo(String code, String name, String explanation, String keyword, String maker, String runtime){
        this.code = code;
        this.name = name;
        this.explanation = explanation;
        this.keyword = keyword;
        this.maker = maker;
        this.runtime = runtime;
    }

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

    public String getExplanation() {
        return explanation;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getMaker() {
        return maker;
    }

    public String getRuntime() {
        return runtime;
    }
}
