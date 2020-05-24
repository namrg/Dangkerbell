package com.example.dankerbell.Firebase;

import java.util.HashMap;
import java.util.Map;

public class profileMapper implements MappingInterface {
    String gender;
    String birthday;
    String diabeteskind;
    double height;
    double weight;
    double bmi;
    String underlyingdisease;
    String highbloodpressure;
    String smoke;
    String year;
    String healdiabetes;
    String activity;
    String weightchange;

    public profileMapper(String gender,String birthday, String diabeteskind,double height,double weight, double bmi,
            String underlyingdisease,
            String highbloodpressure,
            String smoke,
            String year,
            String healdiabetes,
            String activity,
            String weightchange) {
        this.gender = gender;
        this.birthday=birthday;
        this.diabeteskind=diabeteskind;
        this.height=height;
        this.weight=weight;
        this.bmi=bmi;
        this.underlyingdisease=underlyingdisease;
        this.highbloodpressure=highbloodpressure;
        this.smoke=smoke;
        this.year=year;
        this.healdiabetes=healdiabetes;
        this.activity=activity;
        this.weightchange=weightchange;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("성별",gender);
        result.put("생년월일", birthday);
        result.put("당뇨유형", diabeteskind);
        result.put("키", height);
        result.put("몸무게",weight);
        result.put("bmi",bmi);
        result.put("기저질환",underlyingdisease);
        result.put("고혈압",highbloodpressure);
        result.put("흡연", smoke);
        result.put("당뇨유병기간",year);
        result.put("당뇨치료방법", healdiabetes);
        result.put("활동량", activity);
        result.put("체중변화", weightchange);


        return result;    }
}
