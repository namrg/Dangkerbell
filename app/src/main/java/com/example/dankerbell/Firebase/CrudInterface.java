package com.example.dankerbell.Firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public interface CrudInterface {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //각종 기록들 crud
    void create();
    void read();
    void update();
    void delete();
}
