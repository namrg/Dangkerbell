package com.example.dankerbell.AlarmManagement;

public class pillInfo {
    String pillName;//약 이름
    String pilltime;//전,후
    String takingPillTime; //기상, 아침, 점심, 저녁, 취침
    int times; //30분후
    String unit_amount; // 알, 용량
    int amount; // 1알

    public pillInfo(String pillName, String takingPillTime, String pillTime, int times, int amount, String unit_amount) {
        this.pillName = pillName;
        this.takingPillTime = takingPillTime;
        this.pillName = pillName;
        this.times = times;
        this.amount = amount;
        this.unit_amount = unit_amount;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public String getPilltime() {
        return pilltime;
    }

    public void setPilltime(String pilltime) {
        this.pilltime = pilltime;
    }

    public String getTakingPillTime() {
        return takingPillTime;
    }

    public void setTakingPillTime(String takingPillTime) {
        this.takingPillTime = takingPillTime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(String unit_amount) {
        this.unit_amount = unit_amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
