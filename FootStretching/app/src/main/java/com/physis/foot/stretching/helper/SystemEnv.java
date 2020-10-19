package com.physis.foot.stretching.helper;

public class SystemEnv {

    public static String getAnglePhaseValue(String dir, String anglePhase){
        if(dir.equals("1") || dir.equals("2")){
            switch (anglePhase){
                case "0":
                    return "0°";
                case "1":
                    return "15°";
                case "2":
                    return "30°";
                default:
                    return "45°";
            }
        }else{
            switch (anglePhase){
                case "0":
                    return "0°";
                case "1":
                    return "5°";
                default:
                    return "10°";
            }
        }
    }
}
