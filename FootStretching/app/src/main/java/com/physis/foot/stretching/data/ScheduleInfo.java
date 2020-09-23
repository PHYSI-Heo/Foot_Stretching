package com.physis.foot.stretching.data;

public class ScheduleInfo {

    private String no, userCode, patternCode, patternName;
    private String dateTime;
    private int order;
    private boolean fulfill;

    public ScheduleInfo(String no, String patternCode, String patternName, int order, String dateTime, int fulfill){
        this.no = no;
        this.patternCode = patternCode;
        this.patternName = patternName;
        this.order = order;
        this.dateTime = dateTime;
        this.fulfill = fulfill == 1;
    }

    public String getNo() {
        return no;
    }

    public int getOrder() {
        return order;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getPatternCode() {
        return patternCode;
    }

    public String getPatternName() {
        return patternName;
    }

    public String getUserCode() {
        return userCode;
    }

    public boolean getFulfill(){
        return fulfill;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setFulfill(boolean fulfill) {
        this.fulfill = fulfill;
    }

    public void setPatternCode(String patternCode) {
        this.patternCode = patternCode;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }
}
