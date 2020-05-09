package com.example.dankerbell.Firebase;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dankerbell.pillManagement.pillMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FoodlistCrud implements CrudInterface {
    private static FoodlistCrud instance;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    public static FoodlistCrud getInstance() {
        if(instance == null){
            instance = new FoodlistCrud();
        }
        return instance;
    }
    public static Handler mealHandler=new Handler();

    static ArrayList<String> food=new ArrayList<>();
    static ArrayList<String> kcal=new ArrayList<>();
    public static ArrayList<String> getFood() {
        return food;
    }
    public static ArrayList<String> getKcal() {
        return kcal;
    }
    public static String time="";
    @Override
    public void create(){}
    public void create(String time,String date,String food,String kcal) {
        Map<String, Object> updateData = new HashMap<>();
        mealMapper post=new mealMapper(time,date,food,kcal);
        updateData=post.toMap();
        db.collection("user").document(User).collection("meal"+date).document(time)
                .set(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("식단 데이터 추가", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("식단 데이터 추가", "Error writing document", e);
                    }
                });
    }

//    public static void getFoodList(){
//        db.collection("FoodList").document("Food").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists())
//                        food.add(document.getData().get("food").toString());
//                        kcal.add(document.getData().get("kcal").toString());
//                    Log.d("받아오는 음식",food.get(0));
//                    Log.d("받아오는 칼로리",kcal.get(0));
//                    mealHandler.sendEmptyMessage(1001);
//                }
//
//            }
//        });
//    }



    @Override
    public void read(){ // 내 음식 읽기


    }

    public static void readfoodList() {
        db.collection("FoodList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("음식 데이터 읽기", document.getId() + " => " + document.getData().get("food"));
                                food.add(document.getData().get("food").toString());
                                kcal.add(document.getData().get("kcal").toString());
                                Log.d("칼로리 데이터 읽기", document.getId() + " => " + document.getData().get("kcal"));
                                mealHandler.sendEmptyMessage(1001);

                            }
                        } else {
                        }
                    }
                });

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
