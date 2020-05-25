package com.example.dankerbell.Firebase;

import java.util.HashMap;
import java.util.Map;

public class timeMapper implements MappingInterface {
    String wakeuptime;
    String morningtime;
    String lunchtime;
    String dinnertime;
    String sleeptime;

    public timeMapper(String wakeuptime, String morningtime, String lunchtime, String dinnertime, String sleeptime) {
        this.wakeuptime = wakeuptime;
        this.morningtime = morningtime;
        this.lunchtime = lunchtime;
        this.dinnertime = dinnertime;
        this.sleeptime = sleeptime;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("기상시간",wakeuptime);
        result.put("아침식사시간",morningtime);
        result.put("점심식사시간",lunchtime);
        result.put("저녁식사시간",dinnertime);
        result.put("취침시간",sleeptime);

        return result;
    }
}
