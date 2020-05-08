package com.example.dankerbell.Firebase;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FoodlistCrud implements CrudInterface {
    private static FoodlistCrud instance;
    public static boolean getmsg=false;
    public static FoodlistCrud getInstance() {
        if(instance == null){
            instance = new FoodlistCrud();
        }
        return instance;
    }
    public static Handler mealHandler=new Handler();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    static ArrayList<String> food=new ArrayList<>();
    static ArrayList<String> kcal=new ArrayList<>();
    public static ArrayList<String> getFood() {
        return food;
    }
    public static ArrayList<String> getKcal() {
        return kcal;
    }
    @Override
    public void create() {
    }

    public static void getFoodList(){
        db.collection("FoodList").document("Food").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists())
                        food.add(document.getData().get("food").toString());
                        kcal.add(document.getData().get("kcal").toString());
                    Log.d("받아오는 음식",food.get(0));
                    Log.d("받아오는 칼로리",kcal.get(0));
                    mealHandler.sendEmptyMessage(1001);
                }

            }
        });
    }



//    public static void getKcalList(){
//        db.collection("FoodList").document("Food").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists())
//                        kcal.add(document.getData().get("kcal").toString());
//                    Log.d("받아오는 칼로리",kcal.get(0));
//                    mealHandler.sendEmptyMessage(1001);
//
//                    getmsg=true;
//                }
//            }
//        });
//    }
    @Override
    public void read() {
        db.collection("FoodList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("음식 데이터 읽기", document.getId() + " => " + document.getData().get("food"));
                                Log.d("칼로리 데이터 읽기", document.getId() + " => " + document.getData().get("kcal"));

                            }
                        } else {
                            Log.w("혈당 데이터 읽기", "Error getting documents.", task.getException());
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
