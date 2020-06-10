package com.example.dankerbell.Firebase;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dankerbell.pillManagement.pillCrud;
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
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class profileCrud implements CrudInterface {
    private static profileCrud instance;
    public static Handler pHandler =new Handler();

    static String mygender;
    static String mybirthday;
    static String mydiabeteskind; // 당뇨 유형
    static String myheight;
    static String myweight;
    static String mybmi;
    static String myunderlyingdisease; //기저질환
    static String myhighbloodpressure;
    static String mysmoke;
    static String myyear; //당뇨 유병기간
    static String myhealdiabetes; //내 치료 방법
    static String myactivity;
    static String myweightchange;

    public static String getMybirthday() {
        return mybirthday;
    }

    public static String getMydiabeteskind() {
        return mydiabeteskind;
    }

    public static String getMyheight() {
        return myheight;
    }

    public static String getMyweight() {
        return myweight;
    }

    public static String getMybmi() {
        return mybmi;
    }

    public static String getMyunderlyingdisease() {
        return myunderlyingdisease;
    }

    public static String getMyhighbloodpressure() {
        return myhighbloodpressure;
    }

    public static String getMysmoke() {
        return mysmoke;
    }

    public static String getMyyear() {
        return myyear;
    }

    public static String getMyhealdiabetes() {
        return myhealdiabetes;
    }

    public static String getMyactivity() {
        return myactivity;
    }

    public static String getMyweightchange() {
        return myweightchange;
    }

    public static String getMygender() {
        return mygender;
    }

    public static profileCrud getInstance() {
        if(instance == null){
            instance = new profileCrud();
        }
        return instance;
    }
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    @Override
    public void create() {

    }
    public void createprofile(String gender,String birthday, String diabeteskind,double height,double weight, double bmi,
                              String underlyingdisease,
                              String highbloodpressure,
                              String smoke,
                              String year,
                              String healdiabetes,
                              String activity,
                              String weightchange){
        Map<String, Object> updateData = new HashMap<>();

        profileMapper post = new profileMapper(gender,birthday,diabeteskind,height,weight,bmi,underlyingdisease,
        highbloodpressure, smoke,year,healdiabetes, activity, weightchange);
        updateData = post.toMap();

        db.collection("user").document(User).collection("내정보").document("my profile")
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
        db.collection("user").document(User).collection("내정보").document("my profile")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                mygender=document.getData().get("성별").toString();
                                mybirthday=document.getData().get("생년월일").toString();
                                mydiabeteskind=document.getData().get("당뇨유형").toString(); // 당뇨 유형
                                myheight=document.getData().get("키").toString();
                                myweight=document.getData().get("몸무게").toString();
                                mybmi=document.getData().get("bmi").toString();
                                myunderlyingdisease=document.getData().get("기저질환").toString(); //기저질환
                                myhighbloodpressure=document.getData().get("고혈압").toString(); // 고혈압 유무
                                mysmoke=document.getData().get("흡연").toString();
                                myyear=document.getData().get("당뇨유병기간").toString(); //당뇨 유병기간
                                myhealdiabetes=document.getData().get("당뇨치료방법").toString(); //내 치료 방법
                                myactivity=document.getData().get("활동량").toString();
                                myweightchange=document.getData().get("체중변화").toString();


                            }
                            else{ mygender="";
                                mybirthday="";
                                mydiabeteskind=""; // 당뇨 유형
                                myheight="";
                                myweight="";
                                mybmi="";
                                myunderlyingdisease=""; //기저질환
                                myhighbloodpressure="";
                                mysmoke="";
                                myyear=""; //당뇨 유병기간
                                myhealdiabetes=""; //내 치료 방법
                                myactivity="";
                                myweightchange="";
                            }
                        //    mHandler1.sendEmptyMessage(1000);
                            Log.d("READ",mygender);
                            Log.d("READ",mybirthday);

                            Log.d("READ",myhighbloodpressure);
                            Log.d("READ",mydiabeteskind);


                            pHandler.sendEmptyMessage(1007);

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
