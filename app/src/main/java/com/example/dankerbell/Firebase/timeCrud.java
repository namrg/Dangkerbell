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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class timeCrud implements CrudInterface {
    private static timeCrud instance;
    public static Handler timeHandler =new Handler();

    static String mywakeuptime="";
    static String mymorningtime="";
    static String mylunchtime=""; // 당뇨 유형
    static String mydinnertime="";
    static String mysleeptime="";


    public static String getMywakeuptime() {
        return mywakeuptime;
    }

    public static String getMymorningtime() {
        return mymorningtime;
    }

    public static String getMylunchtime() {
        return mylunchtime;
    }

    public static String getMydinnertime() {
        return mydinnertime;
    }

    public static String getMysleeptime() {
        return mysleeptime;
    }

    public static timeCrud getInstance() {
        if(instance == null){
            instance = new timeCrud();
        }
        return instance;
    }
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    @Override
    public void create() {

    }
    public void createsetTime(String wakeuptime,String morningtime, String lunchtime,
                              String dinnertime,
                              String sleeptime){
        Map<String, Object> updateData = new HashMap<>();

        timeMapper post = new timeMapper(wakeuptime,morningtime,lunchtime,dinnertime,sleeptime);
        updateData = post.toMap();

        db.collection("user").document(User).collection("알람설정").document("시간")
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
        db.collection("user").document(User).collection("알람설정").document("시간")
             .get()
                      .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful()){
                          DocumentSnapshot document = task.getResult();
                          if(document.exists()){
                              mywakeuptime=document.getData().get("기상시간").toString();
                              mymorningtime=document.getData().get("아침식사시간").toString();
                              mylunchtime=document.getData().get("점심식사시간").toString();
                              mydinnertime=document.getData().get("저녁식사시간").toString();
                              mysleeptime=document.getData().get("취침시간").toString();
                          }
                          else{ mywakeuptime="";
                                mymorningtime="";
                                mylunchtime="";
                                mydinnertime="";
                                mysleeptime="";
                          }
                                            }
                      timeHandler.sendEmptyMessage(1002);

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
