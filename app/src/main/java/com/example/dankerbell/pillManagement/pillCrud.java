package com.example.dankerbell.pillManagement;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dankerbell.Firebase.CrudInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class pillCrud implements CrudInterface {
    private static pillCrud instance;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();

    public static pillCrud getInstance(){
        if(instance == null){
            instance = new pillCrud();
        }
        return instance;
    }
    @Override
    public void create(){}

    //약 추가 부분은 끝
    public void create(String userId,String pill_name, int amount, String unit_amount,
                       int count, String takingPillTime, String pilltime, int times){
        Map<String, Object> updateData = new HashMap<>();

        pillMapper post = new pillMapper(userId, pill_name, amount, unit_amount, count,
                takingPillTime,pilltime, times);
        updateData = post.toMap();

        db.collection(User).document("takeingPill").collection(pill_name)
                .add(updateData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("약 데이터 추가", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("약 데이터 추가", "Error adding document", e);
                    }
                });

    }

    @Override
    public void read(){}

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
