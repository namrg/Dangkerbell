package com.example.dankerbell.pillManagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.mealActivity;

public class pillActivity2 extends AppCompatActivity { // 복약관리 클래스
    pillCrud mPill = pillCrud.getInstance();
    TextView home; // 당커벨 (어플이름 )
    TextView blood_txt, meal_txt, med_name,amount;
    Button register_btn;
    TextView del_btn;
  //final String pillName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPill.read();
        mPill.mHandler = new Handler(){
            @Override public void handleMessage(Message msg){ // 메세지 받고 실행
                if (msg.what==1002){
                    setContentView(R.layout.activity_pill);
                    final String pillName = mPill.getpillName();
                    final String amount2 = String.valueOf(mPill.getAmount());

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

                    del_btn=findViewById(R.id.del_btn);
                    del_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPill.delete(pillName);
                            if(mPill.getSuccess()=="1"){
                                med_name.setText("");
                                amount.setText("");
                            }
                        }
                    });
                    med_name = findViewById(R.id.med_name); // 의약품명
                    amount = findViewById(R.id.amount);

                    med_name.setText(pillName);
                    amount.setText(amount2);
                }
            }
        };



    }


        //데이터 삭제


}
