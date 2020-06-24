package com.example.dankerbell.Firebase;

import java.util.HashMap;
import java.util.Map;

public class activitymealmeanMapper implements MappingInterface {
    int mealmean=0;
    int activitymean=0;

    public activitymealmeanMapper(int mealmean, int activitymean){
        this.mealmean=mealmean;
        this.activitymean=activitymean;
    }
    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("식단평균",mealmean);
        result.put("걸음수평균",activitymean);
        return result;
    }
}
