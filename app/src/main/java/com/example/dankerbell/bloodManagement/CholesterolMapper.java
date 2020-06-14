package com.example.dankerbell.bloodManagement;

import com.example.dankerbell.Firebase.MappingInterface;

import java.util.HashMap;
import java.util.Map;


public class CholesterolMapper implements MappingInterface {
    String cholesterol;
    String dangwha;
    String date;


    public CholesterolMapper(String cholesterol,String dangwha,String date) {
        this.cholesterol = cholesterol;
        this.dangwha=dangwha;
        this.date=date;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("콜레스테롤",cholesterol);
        result.put("당화혈색소", dangwha);
        result.put("날짜", date);
        return result;    }
}