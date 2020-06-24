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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class activitymealmeanCrud implements CrudInterface {
    private static activitymealmeanCrud instance;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int mmean=0;
    //헤헤헿
    int mactivity=0;
    final String User = user.getEmail();
    public static activitymealmeanCrud getInstance() {
        if(instance == null){
            instance = new activitymealmeanCrud();
        }
        return instance;
    }
    public static Handler mealHandler=new Handler();


    @Override
    public void create(){}
    public void create(int mealmean,int activitymean) {
        Map<String, Object> updateData = new HashMap<>();
        activitymealmeanMapper post=new activitymealmeanMapper(mealmean,activitymean);
        updateData=post.toMap(); //time="아침.점심,저녁; date가 날짜
        db.collection("user").document(User).collection("mean").document("mean")
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
    //
    public void updatemeal(int mealmean) {
        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("식단평균", mealmean);
        db.collection("user").document(User).collection("mean").document("mean")
                .set(data, SetOptions.merge());
    }

    public void updateactivity(int activitymean) {

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("걸음수평균", activitymean);
        db.collection("user").document(User).collection("mean").document("mean")
                .set(data, SetOptions.merge());
    }

    public int getMmean() {
        return mmean;
    }

    public int getMactivity() {
        return mactivity;
    }

    @Override
    public void read(){ // 내 중간값값 기
        db.collection("user").document(User).collection("mean").document("mean")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                mmean=Integer.parseInt(document.getData().get("식단평균").toString());
                                mactivity=Integer.parseInt(document.getData().get("운동량평균").toString());
                                Log.d(this.getClass().getName(),String.valueOf(mmean));
                            }
                            else{
                                mmean=0;
                                mactivity=0;
                            }
                            //      mHandler1.sendEmptyMessage(1000);
                        }
                    }
                });

//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            DocumentSnapshot document = task.getResult();
//                            if(document.exists()){
//                                mymorningfood.add(document.getId().toString());
//                                    mymorningkcal.add(document.getData().get("Kcal").toString());
//                                    Log.d("아침데이터",mymorningfood.get(0));
//                            }
//                            else{   Log.d("아침 데이터 읽기","아침데이터 없음");
//                                 mymorningfood.clear();
//                                  mymorningkcal.clear();
//                            }
//                        }
//                        mealHandler.sendEmptyMessage(1001);
//                    }
//                });
    }


    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }



//    public static int getTotalkcal() {
//        return totalkcal;
//    }
//
//    public static void getTotalk() {
//        totalkcal=morningkcal+lunchkcal+dinnerkcal;
//       // mealHandler.sendEmptyMessage(1002);
//
//    }
}
