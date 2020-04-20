package com.example.dankerbell.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public interface CrudInterface {
    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
    //각종 기록들 crud
    void create();
    void read();
    void update();
    void delete();
}
