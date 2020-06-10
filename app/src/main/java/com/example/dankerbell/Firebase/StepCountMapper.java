package com.example.dankerbell.Firebase;

import java.util.HashMap;
import java.util.Map;

public class StepCountMapper implements MappingInterface {
    int count;

    public StepCountMapper(int count) {
        this.count = count;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("걸음수",count);
        return result;    }
}
