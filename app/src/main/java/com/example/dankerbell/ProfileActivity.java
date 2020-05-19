package com.example.dankerbell;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

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
    TextView toolbar_cart;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // 회원정보 입력 창
        woman=findViewById(R.id.woman); // 성별 중 여자
        man=findViewById(R.id.man); // 성별 중 남자
        back=findViewById(R.id.back); // 뒤로 가기 버튼
        birth=findViewById(R.id.brith); // 생년월일
        toolbar_cart = findViewById(R.id.toolbar_cart);

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

        toolbar_cart.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭

            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"로그아웃 클릭");
                signOut();
            }
        });

    }
    private void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent loginintent = new Intent(getApplicationContext(), LoginInActivity.class);
                        startActivity(loginintent);//액티비티 띄우기 새로 추가 - 로그인 전환
                        finishAffinity();
                    }
                });
    }


}
