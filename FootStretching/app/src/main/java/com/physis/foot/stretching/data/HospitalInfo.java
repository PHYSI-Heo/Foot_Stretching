package com.physis.foot.stretching.data;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;


public class HospitalInfo {

    private static HospitalInfo info = null;
    private String code, name;

    private HospitalInfo(){
    }

    public synchronized static HospitalInfo getInstance(){
        if(info == null)
            info = new HospitalInfo();
        return info;
    }

    public void setInfo(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
