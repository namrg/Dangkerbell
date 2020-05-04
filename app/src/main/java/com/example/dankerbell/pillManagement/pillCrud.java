package com.example.dankerbell.pillManagement;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dankerbell.Firebase.CrudInterface;
import com.example.dankerbell.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class pillCrud implements CrudInterface {
    private static pillCrud instance;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();

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
        //데이터는 가져오는데 어떻게 넘기지?
        db.collection("user").document(User).collection("takingPill")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String pillName = document.getData().get("pill_name").toString();
                                int amount = Integer.parseInt(document.getData().get("amount").toString());
                                Log.d("데이터 있음", document.getId() + " => " + document.getData());
                                Log.d("값", pillName + " => " + amount);
                            }
                        } else {
                            Log.d("읽기 실패", "Error getting documents: ", task.getException());
                        }
                    }
                });
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
}