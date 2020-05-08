package com.example.dankerbell.pillManagement;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;


import com.example.dankerbell.Firebase.CrudInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class pillCrud implements CrudInterface {
    private static pillCrud instance;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    String pillName; //약 이름
    int amount; // 복용량
    public static Handler mHandler =new Handler();

    public static pillCrud getInstance() {
        if (instance == null) {
            instance = new pillCrud();
        }
        return instance;
    }

    @Override
    public void create() {}

    //약 추가 부분은 끝
    public void create(String userId, String pill_name, int amount, String unit_amount,
                       int count, String takingPillTime, String pilltime, int times) {
        Map<String, Object> updateData = new HashMap<>();

        pillMapper post = new pillMapper(userId, pill_name, amount, unit_amount, count,
                takingPillTime, pilltime, times);
        updateData = post.toMap();

        db.collection("user").document(User).collection("takingPill").document(pill_name)
                .set(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("약 데이터 추가", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("약 데이터 추가", "Error writing document", e);
                    }
                });
    }

    @Override
    public void read() {
        db.collection("user").document(User).collection("takingPill")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //  Bundle data = new Bundle();
                        // Message msg= Message.obtain();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pillName = document.getData().get("pill_name").toString();
                                amount = Integer.parseInt(document.getData().get("amount").toString());
                                setpillName(pillName);
                                setAmount(amount);
                                //mHandler.sendEmptyMessage(amount);
                                //    msg.setData(data);
                                //Log.d("데이터 있음", document.getId() + " => " + document.getData());
                                Log.d("값", pillName + " => " + amount);
                                Log.d("겟한 값", getpillName() + " => " + getAmount());
                            }
                        } else {
                            Log.d("읽기 실패", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.d(this.getClass().toString(), "read 끗");
    }

    @Override
    public void update() {

    }

    @Override
    public void delete(){}

    public void delete(String pillName) {
        db.collection(User).document("takingPill").collection(pillName).document(pillName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("삭제 성공", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("삭제 실패", "Error deleting document", e);
                    }
                });
    }
    public void setpillName(String pillName){
        this.pillName = pillName;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public String getpillName(){ return pillName;}
    public int getAmount(){ return amount;}
}