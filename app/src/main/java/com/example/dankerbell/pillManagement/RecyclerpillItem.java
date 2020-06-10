package com.example.dankerbell.pillManagement;

import android.text.BoringLayout;

public class RecyclerpillItem {
    String medname;//약 이름
    int pill_amount;//복용량
    boolean notify; //알람
    boolean del; //삭제

    public RecyclerpillItem(String medname, int pill_amount, boolean notify, boolean del){
        this.medname=medname;
        this.pill_amount=pill_amount;
        this.notify=notify;
        this.del=del;
    }

    public String getMedname() {
        return medname;
    }

    public void setMedname(String medname) {
        this.medname = medname;
    }

    public int getPill_amount() {
        return pill_amount;
    }

    public void setPill_amount(int pill_amount) {
        this.pill_amount = pill_amount;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }
}
