package com.example.dankerbell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.pillManagement.pillActivity;
import com.example.dankerbell.pillManagement.pillCrud;

public class homeActivity extends AppCompatActivity { // 홈화면 클래스
    TextView profile;
    pillCrud mPill = pillCrud.getInstance();
    Button blood_btn,meal_btn,pill_btn;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profile=findViewById(R.id.myprofile); // 내 정보 버튼
        profile.setOnClickListener(new View.OnClickListener() { // 내 정보 버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
        blood_btn=findViewById(R.id.blood_btn);
        meal_btn=findViewById(R.id.meal_btn);
        pill_btn=findViewById(R.id.pill_btn);
        blood_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(blood);//혈당관리 클래스 전환
            }
        });
        meal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meal = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(meal);//식단관리 클래스 전환
            }
        });
        pill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pill = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pill);//복약관리 클래스 전환
            }
        });
    }




}
