package com.example.dankerbell.mealManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.pillManagement.pillActivity;

public class mealActivity extends AppCompatActivity { // 식단관리 클래스
    TextView home;
    TextView blood_txt,pill_txt;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        home=findViewById(R.id.home_txt);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        blood_txt=findViewById(R.id.blood_txt); // 상단에 있는 혈당관리 TextView
        pill_txt=findViewById(R.id.pill_txt); // 상단에 있는 복약관리 TextView
        blood_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 혈당관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent bloodintent = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(bloodintent);// 혈당관리 화면으로 이동하도록 bloodActivity로 전환
            }
        });
        pill_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 복약관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent pillintent = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pillintent);// 복약관리 화면으로 이동하도록 pillActivity로 전환
            }
        });
    }

}
