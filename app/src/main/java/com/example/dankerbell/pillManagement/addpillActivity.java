package com.example.dankerbell.pillManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;

public class addpillActivity extends AppCompatActivity { // 약 직접등록 클래스
    TextView back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpill);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() { // 뒤로 가기 클릭시 실행
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}
