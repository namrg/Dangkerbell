package com.example.dankerbell.Firebase;

import java.util.HashMap;
import java.util.Map;

public class mealMapper implements MappingInterface {
    String time="";
    String date="";
    String food="";
    String kcal="";
    public mealMapper(String time,String date,String food,String kcal){
        this.time=time;
        this.date=date;
        this.food=food;
        this.kcal=kcal;
    }
    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("time",time);
        result.put("date",date);
        result.put("Food",food);
        result.put("Kcal",kcal);
        return result;
    }
}
