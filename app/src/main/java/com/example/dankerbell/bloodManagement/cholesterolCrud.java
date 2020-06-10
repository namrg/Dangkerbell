package com.example.dankerbell.bloodManagement;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dankerbell.Firebase.CrudInterface;
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

public class cholesterolCrud implements CrudInterface {
    private static cholesterolCrud instance;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
//    static ArrayList<String> pillName=new ArrayList<>(); //약 이름
//    static ArrayList<Integer> amount; // 복용량
    static String cholesterol;
    static String dangwha;



    public static Handler cholHandler =new Handler();

    public static cholesterolCrud getInstance() {
        if (instance == null) {
            instance = new cholesterolCrud();
        }
        return instance;
    }


    @Override
    public void create() {}

    @Override
    public void read() {}

    //약 추가 부분은 끝
    public void create(String Colesterol,String dangwha, String date) {
        Map<String, Object> updateData = new HashMap<>();

        CholesterolMapper post = new CholesterolMapper(Colesterol,dangwha,date);
        updateData = post.toMap();

        db.collection("user").document(User).collection("Cholesterol").document(date)
                .set(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("당화혈색소", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("당화혈색소", "Error writing document", e);
                    }
                });
    }

    public static String getCholesterol() {
        return cholesterol;
    }

    public static String getDangwha() {
        return dangwha;
    }

    public void read(String date) { //
        db.collection("user").document(User).collection("Cholesterol").document(date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                cholesterol=document.getData().get("콜레스테롤").toString();
                                dangwha=document.getData().get("당화혈색소").toString();


                            }
                            else{
                                cholesterol="";
                                dangwha="";
                            }

                            cholHandler.sendEmptyMessage(1001);
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



}