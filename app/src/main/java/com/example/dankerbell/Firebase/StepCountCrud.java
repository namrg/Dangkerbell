package com.example.dankerbell.Firebase;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class StepCountCrud implements CrudInterface {
    private static StepCountCrud instance;
    public static Handler stepHandler =new Handler();

    static String step="";

    public static String getstep() {
        return step;
    }

    public static StepCountCrud getInstance() {
        if(instance == null){
            instance = new StepCountCrud();
        }
        return instance;
    }
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    @Override
    public void create() {

    }
    public void createstep(int count){
        Map<String, Object> updateData = new HashMap<>();
        StepCountMapper post = new StepCountMapper(count);
        updateData = post.toMap();

        db.collection("user").document(User).collection("걸음수").document("stepcount")
                .set(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("데이터 추가", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("데이터 추가", "Error writing document", e);
                    }
                });

    }

    @Override
    public void read() {
        db.collection("user").document(User).collection("걸음수").document("stepcount")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                step=document.getData().get("걸음수").toString();

                            }
                            else{
                                step="";
                            }
                        //    mHandler1.sendEmptyMessage(1000);

                            Log.d("DB에서받아온걸음수",step);
                            stepHandler.sendEmptyMessage(1007);

                        }
                    }
                });
    }

    @Override
    public void update() {

    }
    public void updatehw(String height,String weight,double bmi) {

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("키", height);
        data.put("몸무게", weight);
        data.put("bmi", bmi);



        db.collection("user").document(User).collection("내정보").document("my profile")
                .set(data, SetOptions.merge());


    }
    @Override
    public void delete() {

    }



}
