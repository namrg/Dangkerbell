package com.example.dankerbell;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.Firebase.profileCrud;
import com.example.dankerbell.Firebase.timeCrud;

import java.util.Calendar;

public class mysetTimeActivity extends AppCompatActivity {
    TextView wakeuptime,sleeptime,morningmealtime,lunchtime,dinnertime,back;
    profileCrud mprofile = profileCrud.getInstance();
    Button setTimebtn;
    final Calendar cal = Calendar.getInstance();
    String mwakeuptime,msleeptime,mmorningtime,mlunchtime,mdinnertime;
    timeCrud mtimecrud = timeCrud.getInstance(); //firebase 참조 singletone
    TextView first;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysettime);
        wakeuptime=findViewById(R.id.wakeuptime);
        sleeptime=findViewById(R.id.sleeptime);
        morningmealtime=findViewById(R.id.morningmealtime);
        lunchtime=findViewById(R.id.lunchmealtime);
        dinnertime=findViewById(R.id.dinnermealtime);
        back=findViewById(R.id.backspace);
        setTimebtn=findViewById(R.id.setTimereplace);
        first=findViewById(R.id.first);
        mtimecrud.read();
        back.setOnClickListener(new View.OnClickListener() { // 뒤로 가기 버튼 시 실행  왜 실행이 안되지??
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"뒤로가기 클릭");
                onBackPressed();
            }
        }); // 뒤로가기
        mtimecrud.timeHandler = new Handler(){
            @Override public void handleMessage(Message msg){
                if (msg.what==1002){
                    if(mtimecrud.getMywakeuptime().equals("")){
                        first.setVisibility(View.VISIBLE);
                    }
                    first.setVisibility(View.GONE);
                    Log.d(this.getClass().getName(),"메세지 받음");
                    wakeuptime.setText(mtimecrud.getMywakeuptime()); // 기상시간
                    sleeptime.setText(mtimecrud.getMysleeptime()); // 취침
                    morningmealtime.setText(mtimecrud.getMymorningtime()); //아침
                    lunchtime.setText(mtimecrud.getMylunchtime()); //점심
                    dinnertime.setText(mtimecrud.getMydinnertime()); //저녁
                }
            }
        };



        setTimebtn.setOnClickListener(new View.OnClickListener() { //수정클릭
            @Override
            public void onClick(View view) {
           //     mtimecrud.createsetTime(mwakeuptime,mmorningtime,mlunchtime,mdinnertime,msleeptime);
                Intent intent = new Intent(getApplicationContext(), setTimeActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });


    }




}
