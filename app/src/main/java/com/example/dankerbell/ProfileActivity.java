package com.example.dankerbell;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dankerbell.Firebase.profileCrud;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity { // 회원정보등록 클래스

    TextView back;
    profileCrud mprofile = profileCrud.getInstance();

    RadioButton woman,man,hypertension,nonhypertension,smoke,nonsmoke,lessact,normalact,manyact,loseweight,justweight,gainweight;
    TextView birth,bmi;
    EditText height,weight,disease;
    Spinner diabeteskind,healspinner;
    Button store;
    TextView logout;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    TextView toolbar_cart;
    private GoogleSignInClient mGoogleSignInClient;
    String birthday="";
    String smoking="";
    String gender="";
    String highblood="";
    String activity="";
    String weightchange="";
    double iheight=0.0;
    double iweight=0.0;
    double mbmi=0.0;
    String myear="";
    Toolbar toolbar;
    static String sucess="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2); // 회원정보 입력 창
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarprofile);

        woman=findViewById(R.id.woman); // 성별 중 여자
        man=findViewById(R.id.man); // 성별 중 남자
        hypertension=findViewById(R.id.hypertension);//고혈압 유
        nonhypertension=findViewById(R.id.nonhypertension); //고혈압 무
        smoke=findViewById(R.id.smoke);//흡연 유
        nonsmoke=findViewById(R.id.nonsmoke); //흡연 무
        lessact=findViewById(R.id.lessact); // 활동량 적음
        normalact=findViewById(R.id.normalact); //활동량 보통
        manyact=findViewById(R.id.manyact);//활동량 많음
        loseweight=findViewById(R.id.loseweight); // 체중 감소
        justweight=findViewById(R.id.justweight); // 변화없음
        gainweight=findViewById(R.id.gainweight); // 체중 증가
        bmi=findViewById(R.id.bmi);
        height=findViewById(R.id.height);
        weight=findViewById(R.id.weight);
        disease=findViewById(R.id.disease); // 기저질환
        toolbar=findViewById(R.id.toolbar);
        diabeteskind=findViewById(R.id.diabeteskindspinner); //당뇨유형
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.diabeteskind, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        diabeteskind.setAdapter(adapter);
        healspinner=findViewById(R.id.healspinner); //당뇨 치료법
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.heal, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        healspinner.setAdapter(adapter1);
        store=findViewById(R.id.setTimefinish); //저장하기 버튼
        back=findViewById(R.id.backspace); // 뒤로 가기 버튼
        birth=findViewById(R.id.brith); // 생년월일
        TextView yearpicker=findViewById(R.id.yearpicker);
        toolbar_cart=findViewById(R.id.toolbar_cart);

        //당뇨유병기간 선택 dialog
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        final Dialog d = new Dialog(ProfileActivity.this);
        d.setContentView(R.layout.yeardialog);
        Button okBtn = (Button) d.findViewById(R.id.birthday_btn_ok);
        Button cancelBtn = (Button) d.findViewById(R.id.birthday_btn_cancel);
        yearpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.show();
            }
        });
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.birthdayPicker);
        np.setMaxValue(year);
        np.setMinValue(year-70);
        np.setValue(year);
        np.setWrapSelectorWheel(false);
        okBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                yearpicker.setText(String.valueOf(np.getValue()));
                Log.d(this.getClass().getName(),"확인버튼 클릭");
                d.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.d(this.getClass().getName(),"취소버튼 클릭");
                d.dismiss();
            }
        });
        //당뇨유병기간 dialog

        //생년월일
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { //뭔가 이상해
                birth.setText(year + "년" + (monthOfYear+1) + "월" + dayOfMonth + "일"); // 선택한 생년월일 출력
                SimpleDateFormat y=new SimpleDateFormat("yyyy");
                SimpleDateFormat m=new SimpleDateFormat("MM");
                SimpleDateFormat d=new SimpleDateFormat("dd");

                String mybirthmonth=m.format(monthOfYear+1);
                String mybirthday=d.format(dayOfMonth);
                final Calendar calendar = Calendar.getInstance(); // 오늘날짜
                birthday = year+"."+(monthOfYear+1)+"."+dayOfMonth;
                Log.d("생년월일",birthday);
            }
        };
        final DatePickerDialog dialog = new DatePickerDialog(this, listener, 2020, 1, 1);




        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // '생년월일을 입력해주세요' 클릭 시 DatepickerDialog 달력 생성
                dialog.getDatePicker().setSpinnersShown(true);
                dialog.getDatePicker().setCalendarViewShown(false);
                dialog.show(); //DatePickerDialog 생성
            }
        });

        back.setOnClickListener(new View.OnClickListener() { // 뒤로 가기 버튼 시 실행  왜 실행이 안되지??
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"뒤로가기 클릭");

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
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        store.setOnClickListener(new View.OnClickListener() { //저장버튼 클릭릭
            @Override
           public void onClick(View view) {
                String mdiabeteskind = diabeteskind.getSelectedItem().toString();
                String mheal = healspinner.getSelectedItem().toString();
                String mdisease=disease.getText().toString();
                String mheight=height.getText().toString();
                String mweight=weight.getText().toString();
                iweight=Double.parseDouble(mweight);
                iheight=Double.parseDouble(mheight);
                Double iheight2=Double.parseDouble(mheight)/100;
                mbmi=iweight/(iheight2*iheight2);
                myear=String.valueOf(np.getValue());
                mbmi=Double.parseDouble(String.format("%.2f",mbmi));
                //추가
                if (mdiabeteskind.equals("")|mheal.equals("")|mdisease.equals("")|iweight==0.0|iheight==0.0|gender.equals("")|birthday.equals("")|mbmi==0.0|highblood.equals("")|smoking.equals("")|myear.equals("")| activity.equals("")|weightchange.equals("")) {
                    notifyshow();

                } else {
                finishshow();
                mprofile.createprofile(gender,birthday,mdiabeteskind,iheight,iweight,mbmi,mdisease,highblood,smoking,myear,mheal,activity,weightchange);
                    Intent homeintent = new Intent(getApplicationContext(), myprofileActivity.class);
                    startActivity(homeintent);//홈화면 전환
                }


            }
        });

    }
    public void smokeRadio(View v){
        boolean checked=((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.smoke:
                if(checked){
                    smoking="예";

                }
                else{
                    smoking="아니오";
                }
                break;
            case R.id.nonsmoke:
                if(checked){
                    smoking="아니오";

                }
                else{
                    smoking="예";
                }
                break;
        }
    }
    void notifyshow(){
        Log.d(this.getClass().getName(),"정보 다 입력하지 않고 저장하기 클릭");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("빈칸을 채워주세요.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        builder.show();

    }
    void finishshow(){
        Log.d(this.getClass().getName(),"정보 다 입력하고 저장하기 클릭");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정보가 저장되었습니다.");

        builder.show();

    }
    public void GenderRadio(View v){
        boolean checked=((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.woman:
                if(checked){
                    gender="여자";

                }
                else{
                    gender="남자";
                }
                break;
            case R.id.man:
                if(checked){
                    gender="남자";

                }
                else{
                    gender="여자";
                }
                break;
        }
    }
    public void highbloodRadio(View v){
        boolean checked=((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.hypertension:
                if(checked){
                    highblood="유";

                }
                else{
                    highblood="무";
                }
                break;
            case R.id.nonhypertension:
                if(checked){
                    highblood="무";

                }
                else{
                    highblood="유";
                }
                break;
        }
    }
    public void activityRadio(View v){
        boolean checked=((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.lessact:
                if(checked){
                    activity="적음";
                }

                break;
            case R.id.normalact:
                if(checked){
                    activity="보통";
                }
                break;
            case R.id.manyact:
                if(checked){
                    activity="많음";
                }
                break;
        }
    }
    public void weightRadio(View v){
        boolean checked=((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.loseweight:
                if(checked){
                    weightchange="감소";
                }

                break;
            case R.id.justweight:
                if(checked){
                    weightchange="변화없음";
                }
                break;
            case R.id.gainweight:
                if(checked){
                    activity="증가";
                }
                break;
        }
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
