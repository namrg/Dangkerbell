package com.example.dankerbell.pillManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.mealActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static com.example.dankerbell.Firebase.CrudInterface.db;

public class pillActivity extends AppCompatActivity { // 복약관리 클래스
    pillCrud mPill = pillCrud.getInstance();
    TextView home; // 당커벨 (어플이름 )
    TextView blood_txt, meal_txt, med_name;
    Button register_btn,del_btn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill);
        home=findViewById(R.id.home_txt);
        home.setOnClickListener(new View.OnClickListener() { // 당커벨 클릭 시 홈화면으로 전환
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                startActivity(intent);//
            }
        });
        blood_txt=findViewById(R.id.blood_txt); // 상단에 있는 혈당관리 TextView
        meal_txt=findViewById(R.id.meal_txt); // 상단에 있는 식단관리 TextView
        blood_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 혈당관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent bloodintent = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(bloodintent);// 혈당관리 화면으로 이동하도록 bloodActivity로 전환
            }
        });
        meal_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 식단관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent mealintent = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(mealintent);// 식단관리 화면으로 이동하도록 mealActivity로 전환
            }
        });
        register_btn=findViewById(R.id.register_btn); // 약 직접등록하기 버튼
        register_btn.setOnClickListener(new View.OnClickListener() { // 약 직접등록하기 버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent addpillintent = new Intent(getApplicationContext(), addpillActivity.class);
                startActivity(addpillintent);// 약 직접등록 화면으로 이동하도록 addpillActivity로 전환
            }
        });
    }

    public void onStart() {
        super.onStart();
        //데이터 읽기
        mPill.read();

    /*    //데이터 삭제
        med_name = findViewById(R.id.med_name);
        final String pillName = med_name.getText().toString();
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPill.delete(pillName);
            }
        });*/
    }
}

