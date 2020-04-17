package com.example.dankerbell.Firebase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FirebasePost {
    private DatabaseReference mPostReference;

    public void postFirebaseDatabase(String userId, double bloodsugar, double bloodpressure, double cholesterol, double hbA1c, Date date){
        //혈당 기록 추가 메소드 (이 클래스 모듈화를 해야할듯,,
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        //if(add){
            MappingData post = new MappingData(userId, bloodsugar, bloodpressure, cholesterol, hbA1c, date);
            postValues = post.toMap();
        //}
        childUpdates.put("/id_list/" + userId, postValues);
        mPostReference.updateChildren(childUpdates);
    }
}
