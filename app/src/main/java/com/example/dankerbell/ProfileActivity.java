package com.example.dankerbell;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity { // 회원정보등록 클래스
    TextView back;
    RadioButton woman,man;
    TextView birth,bmi;
    EditText height,weight;
    Button store;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // 회원정보 입력 창
        woman=findViewById(R.id.woman); // 성별 중 여자
        man=findViewById(R.id.man); // 성별 중 남자
        back=findViewById(R.id.back); // 뒤로 가기 버튼
        birth=findViewById(R.id.brith); // 생년월일

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birth.setText(year + "년" + (monthOfYear+1) + "월" + dayOfMonth + "일"); // 선택한 생년월일 출력
            }
        };
        final DatePickerDialog dialog = new DatePickerDialog(this, listener, 2020, 1, 1);

        man.setOnClickListener(new View.OnClickListener() { // 남자 선택  시 실행되는 메소드
            boolean clicked=true;

            @Override
            public void onClick(View view) {
                if(clicked){ //버튼 클릭 시 실행
                    man.setBackgroundColor(Color.rgb(130,190,230)); // 남자 클릭 시 배경색 파랑색
                    man.setTextColor(Color.rgb(255,255,255)); // 남자 클릭 시 글자색 흰색
                    clicked=false;
                }
                else{
                    man.setBackgroundColor(Color.rgb(255,255,255)); // 남자 다시 클릭 시 배경색 흰색
                    man.setTextColor(Color.rgb(184,184,184)); // 남자 다시 클릭 시 글자색 회색

                    clicked=true;

                }


                }
        });

        woman.setOnClickListener(new View.OnClickListener() { // 여자 선택  시 실행되는 메소드
            boolean clicked=true;

            @Override
            public void onClick(View view) {

            }
        });


        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // '생년월일을 입력해주세요' 클릭 시 DatepickerDialog 달력 생성
                dialog.getDatePicker().setSpinnersShown(true);
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show(); //DatePickerDialog 생성
            }
        });
        back.setOnClickListener(new View.OnClickListener() { // 뒤로 가기 버튼 시 실행
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        }); // 뒤로가기


    }



}
