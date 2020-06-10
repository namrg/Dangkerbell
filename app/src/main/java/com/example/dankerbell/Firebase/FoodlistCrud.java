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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class FoodlistCrud implements CrudInterface {
    private static FoodlistCrud instance;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static ArrayList<String> mymorningfood=new ArrayList<>();
    public static ArrayList<String> mymorningkcal=new ArrayList<>();

    public static ArrayList<String> mylunchfood=new ArrayList<>();
    public static ArrayList<String> mylunchkcal=new ArrayList<>();

    public static ArrayList<String> mydinnerfood=new ArrayList<>();
    public static ArrayList<String> mydinnerkcal=new ArrayList<>();

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
        updateData=post.toMap(); //time="아침.점심,저녁; date가 날짜
        db.collection("user").document(User).collection(time+date).document(food)
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

//        db.collection("user").document(User).collection(time+date).document(food)
//                .add(updateData)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()) {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("데이터 추가", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("데이터 추가", "Error writing document", e);
//                    }
//                });

    }

    @Override
    public void read(){ // 내 음식 읽기

    }
    public void readmymorningmeal(final String date){ // 내 음식 읽기
        mymorningfood.clear();
        mymorningkcal.clear();
        Log.d("아침 데이터 읽기",date);
        db.collection("user").document(User).collection("아침"+date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!"".equals(document.getId())) {
                                    Log.d("아침 데이터 읽기", document.getId() + " => " + document.getData().get("Kcal"));
                                    mymorningfood.add(document.getData().get(document.getId()).toString());
                                    mymorningkcal.add(document.getData().get("Kcal").toString());
                                }
                                else{
                                    mymorningfood.clear();
                                    mymorningkcal.clear();
                                }
                            }
                        } else {
                            Log.w("혈당 데이터 읽기", "Error getting documents.", task.getException());
                        }
                        Log.d("배열사이즈!!!", String.valueOf(mymorningfood.size()));

                        mealHandler.sendEmptyMessage(1001);

                    }
                });

    }

    public void readmylunchmeal(String date){ // 내 점심 읽기
        mylunchfood.clear();
        mylunchkcal.clear();
        db.collection("user").document(User).collection("점심"+date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!"".equals(document.getId())) {
                                    Log.d("점심 데이터 읽기", document.getId() + " => " + document.getData().get("Kcal"));
                                    mylunchfood.add(document.getData().get(document.getId()).toString());
                                    mylunchkcal.add(document.getData().get("Kcal").toString());
                                }
                                else{
                                    mylunchfood.clear();
                                    mylunchkcal.clear();
                                }
                            }
                        } else {
                            Log.w("혈당 데이터 읽기", "Error getting documents.", task.getException());
                        }
                        Log.d("배열사이즈!!!", String.valueOf(mylunchfood.size()));

                        mealHandler.sendEmptyMessage(1001);

                    }
                });

    }
    public void readmydinnermeal(String date){ // 내 저녁 읽기
        mydinnerfood.clear();
        mydinnerkcal.clear();
        db.collection("user").document(User).collection("저녁"+date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!document.getId().equals("")) {
                                    Log.d("저녁 데이터 읽기", document.getId() + " => " + document.getData().get("Kcal"));
                                    mydinnerfood.add(document.getData().get(document.getId()).toString());
                                    mydinnerkcal.add(document.getData().get("Kcal").toString());
                                }
                                else{
                                    mydinnerfood.clear();
                                    mydinnerkcal.clear();
                                }
                            }
                        } else {
                            Log.w("혈당 데이터 읽기", "Error getting documents.", task.getException());
                        }
                        mealHandler.sendEmptyMessage(1001);

                    }
                });

    }

    public static void readfoodList() { // 파이어베이스에서 읽어옴
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
