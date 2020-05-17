package com.example.dankerbell;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.pillManagement.pillActivity;
import com.example.dankerbell.pillManagement.pillCrud;

public class homeActivity extends AppCompatActivity { // 홈화면 클래스
    TextView profile;
    pillCrud mPill = pillCrud.getInstance();
    Button blood_btn,meal_btn,pill_btn;
    TextView bluetooth;
    TextView toolbar;
    TextView close;
    DrawerLayout drawerLayout;
    View drawerView;
    Button logout;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.toolbar_menu);
        drawerLayout=findViewById(R.id.drawer_layout) ;
        logout = findViewById(R.id.logout);
        drawerView=findViewById(R.id.drawer);
        close=findViewById(R.id.toolbar_close);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent drawer = new Intent(getApplicationContext(), DrawerActivity.class);
//                startActivity(drawer);//액티비티 띄우기
                drawerLayout.openDrawer(drawerView);


            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        //btn_close = findViewById(R.id.btn_close);
        logout.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"로그아웃 클릭");
            }
        });
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
        bluetooth=findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bluetootintent=new Intent(getApplicationContext(),BluetoothActivity.class);
                startActivity(bluetootintent);
            }
        });
    }




}
