package com.example.dankerbell.bloodManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.R;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.pillManagement.pillActivity;

import java.util.Date;

public class bloodActivity extends AppCompatActivity { // 혈당관리클래스

    BloodSugarCrud mBloodSugar = BloodSugarCrud.getInstance(); //firebase 참조 singletone

    TextView home; //
    TextView meal_txt; // 상단에 식단관리 TextView
    TextView pill_txt; // 상단에 복약관리 TextView
    TextView wakeup,wakeUp,wakeupbloodedit,wakesugartext, wakepressuretext,wakeupbloodfinish;  // 기상 후 !!!
    // wakeUp,wakeup=기상후 , wakeupbloodedit : 기상 후 연필 아이콘 , wakesugartext : 기상 후 혈당 text wakepressuretext : 기상 후 혈압text
    // wakepressuretext : 기상 후 혈압 text wakeupbloodfinish : 체크 아이콘
    EditText wakesugaredit, wakepressureedit; // 기상 후 혈당입력칸 / 혈압 입력칸

    TextView Morning,morning,morningbloodedit,morningsugartext, morningpressuretext,morningbloodfinish;  // 아침 !!!
    EditText morningsugaredit, morningpressureedit; // 아침 혈당입력칸 / 혈압 입력칸

    TextView Lunch,lunch,lunchbloodedit,lunchsugartext, lunchpressuretext,lunchbloodfinish;  // 점심 !!!
    // Lunch,lunch : 점심 , lunchbloodedit : 점심 연필 아이콘 , lunchsugartext : 점심 혈당 text, lunchbloodfinish : 점심 혈압 text
    // lunchpressuretext : 점심 혈압 text lunchbloodfinish : 체크 아이콘
    EditText lunchsugaredit, lunchpressureedit; // 점심 혈당입력칸 / 혈압 입력칸

    TextView Dinner,dinner,dinnerbloodedit,dinnersugartext, dinnerpressuretext,dinnerbloodfinish;  // 저녁 !!!
    EditText dinnersugaredit, dinnerpressureedit; // 저녁 혈당입력칸 / 혈압 입력칸

    TextView Sleep,sleep,sleepbloodedit,sleepsugartext, sleeppressuretext,sleepbloodfinish;  // 취침전 !!!
    EditText sleepsugaredit, sleeppressureedit; // 취침 전 혈당입력칸 / 혈압 입력칸

    String time; // 기상후, 식전 식후 등


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
        home=findViewById(R.id.home_txt);
        meal_txt=findViewById(R.id.meal_txt);
        pill_txt=findViewById(R.id.pill_txt);

        //기상 후
        wakeup=findViewById(R.id.wakeup);
        wakeUp=findViewById(R.id.wakeUp);
        wakeupbloodedit=(TextView)findViewById(R.id.wakeupbloodedit);
        wakesugartext=findViewById(R.id.wakesugartext);
        wakepressuretext=findViewById(R.id.wakepressureetext);
        wakeupbloodfinish=findViewById(R.id.wakeupbloodfinish);
        wakesugaredit=findViewById(R.id.wakesugaredit);
        wakepressureedit=findViewById(R.id.wakepressureedit);

        //아침
        morning=findViewById(R.id.morning);
        Morning=findViewById(R.id.Morning);
        morningbloodedit=findViewById(R.id.morningbloodedit);
        morningsugartext=findViewById(R.id.morningsugartext);
        morningpressuretext=findViewById(R.id.morningpressuretext);
        morningbloodfinish=findViewById(R.id.morningbloodfinish);
        morningsugaredit=findViewById(R.id.morningsugaredit);
        morningpressureedit=findViewById(R.id.morningpressureedit);
        //점심
        lunch=findViewById(R.id.lunch);
        Lunch=findViewById(R.id.Lunch);
        lunchbloodedit=findViewById(R.id.lunchbloodedit);
        lunchsugartext=findViewById(R.id.lunchsugartext);
        lunchpressuretext=findViewById(R.id.lunchpressuretext);
        lunchbloodfinish=findViewById(R.id.lunchbloodfinish);
        lunchsugaredit=findViewById(R.id.lunchsugaredit);
        lunchpressureedit=findViewById(R.id.lunchpressureedit);
        //저녁
        dinner=findViewById(R.id.dinner);
        Dinner=findViewById(R.id.Dinner);
        dinnerbloodedit=findViewById(R.id.dinnerbloodedit);
        dinnersugartext=findViewById(R.id.dinnersugartext);
        dinnerpressuretext=findViewById(R.id.dinnerpressuretext);
        dinnerbloodfinish=findViewById(R.id.dinnerbloodfinish);
        dinnersugaredit=findViewById(R.id.dinnersugaredit);
        dinnerpressureedit=findViewById(R.id.dinnerpressureedit);
        //취침 후
        sleep=findViewById(R.id.sleep);
        Sleep=findViewById(R.id.Sleep);
        sleepbloodedit=findViewById(R.id.sleepbloodedit);
        sleepsugartext=findViewById(R.id.sleepsugartext);
        sleeppressuretext=findViewById(R.id.sleeppressuretext);
        sleepbloodfinish=findViewById(R.id.sleepbloodfinish);
        sleepsugaredit=findViewById(R.id.sleepsugaredit);
        sleeppressureedit=findViewById(R.id.sleeppressureedit);

        wakeupbloodedit.setOnClickListener(new View.OnClickListener() { // 기상 후 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야 냥냥경 바보쓰쓰");
               wakeUp.setVisibility(View.INVISIBLE);

                wakeupbloodedit.setVisibility(View.INVISIBLE);
                wakesugartext.setVisibility(View.INVISIBLE);
                wakepressuretext.setVisibility(View.INVISIBLE);
                wakeupbloodfinish.setVisibility(View.VISIBLE);
                wakesugaredit.setVisibility(View.VISIBLE);
                wakepressureedit.setVisibility(View.VISIBLE);
                wakeup.setVisibility(View.VISIBLE);

            }
        });

        wakeupbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 O
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                wakeup.setVisibility(View.INVISIBLE);
                wakeUp.setVisibility(View.VISIBLE);
                wakeupbloodedit.setVisibility(View.VISIBLE);
                wakesugartext.setVisibility(View.VISIBLE);
                wakepressuretext.setVisibility(View.VISIBLE);
                wakeupbloodfinish.setVisibility(View.INVISIBLE);
                wakesugaredit.setVisibility(View.INVISIBLE);
                wakepressureedit.setVisibility(View.INVISIBLE);
                String wakesugar=(String)wakesugaredit.getText().toString();
                wakesugartext.setText(wakesugar);

                String wakepressure = (String)wakepressureedit.getText().toString();
                wakepressuretext.setText(wakepressure);

                /*
                bloodsugar : userid - { bs, bp, date, time }
                 */
                // DB 삽입부
                time = wakeup.toString();
                mBloodSugar.create("userid", Double.parseDouble(wakesugartext.toString()), Double.parseDouble(wakepressuretext.toString()), new Date(), time);

            }

        });
        morningbloodedit.setOnClickListener(new View.OnClickListener() { // 아침 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Morning.setVisibility(View.INVISIBLE);
                morningbloodedit.setVisibility(View.INVISIBLE);
                morningsugartext.setVisibility(View.INVISIBLE);
                morningpressuretext.setVisibility(View.INVISIBLE);
                morningbloodfinish.setVisibility(View.VISIBLE);
                morningsugaredit.setVisibility(View.VISIBLE);
                morningpressureedit.setVisibility(View.VISIBLE);
                morning.setVisibility(View.VISIBLE);

            }
        });

        morningbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 X
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                morning.setVisibility(View.INVISIBLE);
                Morning.setVisibility(View.VISIBLE);
                morningbloodedit.setVisibility(View.VISIBLE);
                morningsugartext.setVisibility(View.VISIBLE);
                morningpressuretext.setVisibility(View.VISIBLE);
                morningbloodfinish.setVisibility(View.INVISIBLE);
                morningsugaredit.setVisibility(View.INVISIBLE);
                morningpressureedit.setVisibility(View.INVISIBLE);
                String morningsugar = (String) morningsugaredit.getText().toString();
                morningsugartext.setText(morningsugar); // 입력한 아침 혈당을 morningsugartext에 입력

                String morningpressure = (String)morningpressureedit.getText().toString();
                morningpressuretext.setText(morningpressure); // 입력한 아침 혈압을 morningpressuretext에 입력
            }

        });

        lunchbloodedit.setOnClickListener(new View.OnClickListener() { // 점심 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Lunch.setVisibility(View.INVISIBLE);
                lunchbloodedit.setVisibility(View.INVISIBLE);
                lunchsugartext.setVisibility(View.INVISIBLE);
                lunchpressuretext.setVisibility(View.INVISIBLE);
                lunchbloodfinish.setVisibility(View.VISIBLE);
                lunchsugaredit.setVisibility(View.VISIBLE);
                lunchpressureedit.setVisibility(View.VISIBLE);
                lunch.setVisibility(View.VISIBLE);
            }
        });

        lunchbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 X
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                lunch.setVisibility(View.INVISIBLE);
                Lunch.setVisibility(View.VISIBLE);
                lunchbloodedit.setVisibility(View.VISIBLE);
                lunchsugartext.setVisibility(View.VISIBLE);
                lunchpressuretext.setVisibility(View.VISIBLE);
                lunchbloodfinish.setVisibility(View.INVISIBLE);
                lunchsugaredit.setVisibility(View.INVISIBLE);
                lunchpressureedit.setVisibility(View.INVISIBLE);
                String lunchsugar = (String)lunchsugaredit.getText().toString(); // 입력한 점심 혈당을 lunchsugaredit에 입력
                lunchsugartext.setText(lunchsugar);

                String lunchpressure = (String)lunchpressureedit.getText().toString(); // 입력한 점심 혈압을 lunchpressureedit에 입력
                lunchpressuretext.setText(lunchpressure);
            }

        });

        dinnerbloodedit.setOnClickListener(new View.OnClickListener() { // 저녁 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Dinner.setVisibility(View.INVISIBLE);
                dinnerbloodedit.setVisibility(View.INVISIBLE);
                dinnersugartext.setVisibility(View.INVISIBLE);
                dinnerpressuretext.setVisibility(View.INVISIBLE);
                dinnerbloodfinish.setVisibility(View.VISIBLE);
                dinnersugaredit.setVisibility(View.VISIBLE);
                dinnerpressureedit.setVisibility(View.VISIBLE);
                dinner.setVisibility(View.VISIBLE);
            }
        });

        dinnerbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 X
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                dinner.setVisibility(View.INVISIBLE);
                Dinner.setVisibility(View.VISIBLE);
                dinnerbloodedit.setVisibility(View.VISIBLE);
                dinnersugartext.setVisibility(View.VISIBLE);
                dinnerpressuretext.setVisibility(View.VISIBLE);
                dinnerbloodfinish.setVisibility(View.INVISIBLE);
                dinnersugaredit.setVisibility(View.INVISIBLE);
                dinnerpressureedit.setVisibility(View.INVISIBLE);
                String dinnersugar = (String)dinnersugaredit.getText().toString(); // 입력한 저녁 혈당을 lunchsugaredit에 입력
                dinnersugartext.setText(dinnersugar);

                String lunchpressure = (String)dinnerpressureedit.getText().toString(); // 입력한 저녁 혈압을 lunchpressureedit에 입력
                dinnerpressuretext.setText(lunchpressure);
            }

        });

        sleepbloodedit.setOnClickListener(new View.OnClickListener() { // 취침 후  행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Sleep.setVisibility(View.INVISIBLE);
                sleepbloodedit.setVisibility(View.INVISIBLE);
                sleepsugartext.setVisibility(View.INVISIBLE);
                sleeppressuretext.setVisibility(View.INVISIBLE);
                sleepbloodfinish.setVisibility(View.VISIBLE);
                sleepsugaredit.setVisibility(View.VISIBLE);
                sleeppressureedit.setVisibility(View.VISIBLE);
                sleep.setVisibility(View.VISIBLE);
            }
        });

        sleepbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 X
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                sleep.setVisibility(View.INVISIBLE);
                Sleep.setVisibility(View.VISIBLE);
                sleepbloodedit.setVisibility(View.VISIBLE);
                sleepsugartext.setVisibility(View.VISIBLE);
                sleeppressuretext.setVisibility(View.VISIBLE);
                sleepbloodfinish.setVisibility(View.INVISIBLE);
                sleepsugaredit.setVisibility(View.INVISIBLE);
                sleeppressureedit.setVisibility(View.INVISIBLE);
                String sleepsugar = (String)sleepsugaredit.getText().toString(); // 입력한 취침 후  혈당을 dinnersugaredit에 입력
                sleepsugartext.setText(sleepsugar);

                String sleeppressure = (String)sleeppressureedit.getText().toString(); // 입력한 취침 후  혈압을 dinnerpressureedit에 입력
                sleeppressuretext.setText(sleeppressure);
            }

        });


        home.setOnClickListener(new View.OnClickListener() { // 당커벨 클릭 시 홈화면으로 전환
            @Override
            public void onClick(View view) {
                Intent homeintent = new Intent(getApplicationContext(), homeActivity.class);
                startActivity(homeintent);// 홈화면 전환하도록 homeActivity로 전환
            }
        });
        meal_txt.setOnClickListener(new View.OnClickListener() { //상단에 식단관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent mealintent = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(mealintent);// 식단관리 화면으로 이동하도록 mealActivity로 전환
            }
        });
        pill_txt.setOnClickListener(new View.OnClickListener() { // 상단에 복약관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent pillintent = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pillintent);// 복약관리 화면으로 이동하도록 pillActivity로 전환
            }
        });
    }
}