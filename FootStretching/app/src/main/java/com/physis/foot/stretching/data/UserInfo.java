package com.physis.foot.stretching.data;

public class UserInfo {
    private String code, name, phone;

    public UserInfo(String code, String name, String phone){
        this.code = code;
        this.name = name;
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
