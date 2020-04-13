package com.example.dankerbell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;

public class homeActivity extends AppCompatActivity {
    TextView profile;
    public void onCreate(Bundle savedInstanceState) {
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

    }



}
