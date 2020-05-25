package com.example.dankerbell;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.Firebase.profileCrud;
import com.example.dankerbell.Firebase.timeCrud;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.security.AccessController.getContext;

public class setTimeActivity extends AppCompatActivity {
    TextView wakeuptime,sleeptime,morningmealtime,lunchtime,dinnertime,back;
    Button setTimebtn;


    final Calendar cal = Calendar.getInstance();
    String mwakeuptime,msleeptime,mmorningtime,mlunchtime,mdinnertime;
    timeCrud mtimecrud = timeCrud.getInstance(); //firebase 참조 singletone



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);
        wakeuptime=findViewById(R.id.wakeuptime);
        sleeptime=findViewById(R.id.sleeptime);
        morningmealtime=findViewById(R.id.morningmealtime);
        lunchtime=findViewById(R.id.lunchmealtime);
        dinnertime=findViewById(R.id.dinnermealtime);
        back=findViewById(R.id.backspace);
        setTimebtn=findViewById(R.id.setTimefinish);
        back.setOnClickListener(new View.OnClickListener() { // 뒤로 가기 버튼 시 실행  왜 실행이 안되지??
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"뒤로가기 클릭");
                onBackPressed();

            }
        }); // 뒤로가기
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) { // 기상  시간
                wakeuptime.setText(i+"시"+i1+"분");
                mwakeuptime=i+":"+i1;


            }
        };
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, listener, 0, 0, false);

        TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) { // 아침 시간

                morningmealtime.setText(i+"시"+i1+"분");
                mmorningtime=i+":"+i1;
                Log.d("2",mwakeuptime);

            }
        };
        final TimePickerDialog timePickerDialog2 = new TimePickerDialog(this, listener2, 0, 0, false);

        TimePickerDialog.OnTimeSetListener listener3 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) { // 점심 시간

                lunchtime.setText(i+"시"+i1+"분");
                mlunchtime=i+":"+i1;
                Log.d("2",mlunchtime);

            }
        };
        final TimePickerDialog timePickerDialog3 = new TimePickerDialog(this, listener3, 0, 0, false);

        TimePickerDialog.OnTimeSetListener listener4 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) { // 저녁 시간

                dinnertime.setText(i+"시"+i1+"분");
                mdinnertime=i+":"+i1;
                Log.d("2",mdinnertime);

            }
        };
        final TimePickerDialog timePickerDialog4 = new TimePickerDialog(this, listener4, 0, 0, false);

        TimePickerDialog.OnTimeSetListener listener5 = new TimePickerDialog.OnTimeSetListener() { // 취침시간
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                sleeptime.setText(i+"시"+i1+"분");
                msleeptime=i+":"+i1;
                Log.d("2",msleeptime);

            }
        };
        final TimePickerDialog timePickerDialog5 = new TimePickerDialog(this, listener5, 0, 0, false);

        wakeuptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show(); //DatePickerDialog 생성
            }
        });
        morningmealtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   showmorningtime();
                timePickerDialog2.show(); //DatePickerDialog 생성
//
//                morningmealtime.setText(h2+":"+mi2);
//                mmorningtime=String.valueOf(h2+":"+mi2);
//                Log.d("1", String.valueOf(morningmealtime));
//                mmorningtime = (String)wakeuptime.getText().toString();

            }
        });
        lunchtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog3.show(); //DatePickerDialog 생성

//                showlunchtime();
//                lunchtime.setText(h3+":"+mi3);
//                mlunchtime=String.valueOf(h3+":"+mi3);
             //   mlunchtime=lunchtime.getText().toString();
            }
        });
        dinnertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog4.show(); //DatePickerDialog 생성

//                showdinnertime();
//                dinnertime.setText(h4+":"+mi4);
//                mdinnertime=String.valueOf(h4+":"+mi4);
            }
        });
        sleeptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog5.show(); //DatePickerDialog 생성

//                showsleeptime();
//                sleeptime.setText(h5+":"+mi5);
//                msleeptime=String.valueOf(h5+":"+mi5);

            }
        });

        setTimebtn.setOnClickListener(new View.OnClickListener() { //저장클릭
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"저장하기 버튼 클릭");

                Log.d(this.getClass().getName(),mwakeuptime);
                Log.d(this.getClass().getName(),mmorningtime);
                Log.d(this.getClass().getName(),mlunchtime);
                Log.d(this.getClass().getName(),mdinnertime);
                Log.d(this.getClass().getName(),msleeptime);

                mtimecrud.createsetTime(mwakeuptime,mmorningtime,mlunchtime,mdinnertime,msleeptime);
                Intent intent = new Intent(getApplicationContext(), mysetTimeActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });


    }



}
