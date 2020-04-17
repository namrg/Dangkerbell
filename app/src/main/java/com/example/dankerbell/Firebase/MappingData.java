package com.example.dankerbell.Firebase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MappingData {
    // 데이터 잠시 담아두고 파이어베이스 사용 모듈화

    String userId; //사용자 id
    String bsId;
    double bloodsugar; //혈당
    double bloodpressure; //혈압
    double cholesterol; //콜레스테롤
    double hbA1c; //당화혈색소(단위 %)
    Date date; //날짜 시간이랑 분리 할지 말지,,

    public MappingData(){
        //기본생성자
    }

    public MappingData(String userId, double bloodsugar, double bloodpressure, double cholesterol, double hbA1c, Date date){
        this.userId = userId;
        this.bloodsugar = bloodsugar;
        this.bloodpressure = bloodpressure;
        this.cholesterol = cholesterol;
        this.hbA1c = hbA1c;
        this.date = new Date();
    }

    public Map<String, Object> toMap(){
        //매핑메소드
        // 필요없으면 삭제할것
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("bloodsugar", bloodsugar);
        result.put("bloospressure", bloodpressure);
        result.put("cholesterol", cholesterol);
        result.put("hbA1c", hbA1c);
        result.put("date", date);

        return result;
    }
}
