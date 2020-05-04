package com.example.dankerbell.pillManagement;

import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.dankerbell.Firebase.MappingInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class pillMapper implements MappingInterface {
    private String userId; //사용자 이름
    private String pill_name; //약 이름
    private int amount; //복용량
    private String unit_amount; // 복용량 단위
    private int count; //복용 횟수
    private String takingPillTime; // 복용시간
    private String pilltime; //식전, 식후
    private int times; // 몇분후

    public pillMapper(String userId, String pill_name, int amount, String unit_amount,
                      int count, String takingPillTime, String pilltime, int times ){
        this.userId = userId;
        this.pill_name = pill_name;
        this.amount = amount;
        this.unit_amount = unit_amount;
        this.count = count;
        this.takingPillTime = takingPillTime;
        this.pilltime = pilltime;
        this.times = times;
    }

    @Override
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId",userId);
        result.put("pill_name", pill_name);
        result.put("amount", amount);
        result.put("unit_amount", unit_amount);
        result.put("count",count);
        result.put("takingPillTime",takingPillTime);
        result.put("pilltime",pilltime);
        result.put("times", times);
        return result;
    }

}
