package com.physis.foot.stretching.data;

public class PatternItemInfo {
    private String code, leftMoving, rightMoving;
    private int order;

    public PatternItemInfo(String code, int order, String leftMoving, String rightMoving){
        this.code = code;
        this.order = order;
        this.leftMoving = leftMoving;
        this.rightMoving = rightMoving;
    }

    public String getCode() {
        return code;
    }

    public int getOrder() {
        return order;
    }

    public String getLeftMoving() {
        return leftMoving;
    }

    public String getRightMoving() {
        return rightMoving;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLeftMoving(String leftMoving) {
        this.leftMoving = leftMoving;
    }

    public void setRightMoving(String rightMoving) {
        this.rightMoving = rightMoving;
    }
}
