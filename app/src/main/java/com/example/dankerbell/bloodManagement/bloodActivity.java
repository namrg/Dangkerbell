package com.example.dankerbell.bloodManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.BloodReporter;
import com.example.dankerbell.bloodManagement.glucoseReporter;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.pillManagement.pillActivity;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class bloodActivity extends AppCompatActivity{ // 혈당관리클래스
    BloodSugarCrud mBloodSugar = BloodSugarCrud.getInstance(); //firebase 참조 singletone
    TextView home; //
    TextView meal_txt; // 상단에 식단관리 TextView
    TextView pill_txt; // 상단에 복약관리 TextView
    TextView wakeup,wakeUp,wakeupbloodedit,wakesugartext, wakepressuretext,wakeupbloodfinish;  // 기상 후 !!!
    // wakeUp,wakeup=기상후 , wakeupbloodedit : 기상 후 연필 아이콘 , wakesugartext : 기상 후 혈당 text wakepressuretext : 기상 후 혈압text
    // wakepressuretext : 기상 후 혈압 text wakeupbloodfinish : 체크 아이콘
    EditText wakesugaredit, wakepressureedit,wakepressureedit2; // 기상 후 혈당입력칸 / 혈압 입력칸

    TextView Morning,morning,morningbloodedit,morningsugartext, morningpressuretext,morningbloodfinish;  // 아침 !!!
    EditText morningsugaredit, morningpressureedit,morningpressureedit2; // 아침 혈당입력칸 / 혈압 입력칸

    TextView Lunch,lunch,lunchbloodedit,lunchsugartext, lunchpressuretext,lunchbloodfinish;  // 점심 !!!
    // Lunch,lunch : 점심 , lunchbloodedit : 점심 연필 아이콘 , lunchsugartext : 점심 혈당 text, lunchbloodfinish : 점심 혈압 text
    // lunchpressuretext : 점심 혈압 text lunchbloodfinish : 체크 아이콘
    EditText lunchsugaredit, lunchpressureedit; // 점심 혈당입력칸 / 혈압 입력칸

    TextView Dinner,dinner,dinnerbloodedit,dinnersugartext, dinnerpressuretext,dinnerbloodfinish;  // 저녁 !!!
    EditText dinnersugaredit, dinnerpressureedit; // 저녁 혈당입력칸 / 혈압 입력칸

    TextView Sleep,sleep,sleepbloodedit,sleepsugartext, sleeppressuretext,sleepbloodfinish;  // 취침전 !!!
    EditText sleepsugaredit, sleeppressureedit; // 취침 전 혈당입력칸 / 혈압 입력칸
    TableLayout wakeuptext,wakeupedit;

    TextView currentdate;
    TextView prev,next;
    private static Handler mHandler ;
    String time; // 기상후, 식전 식후 등
    public static final String APP_TAG = "SimpleHealth";

    //@BindView(R.id.editHealthDateValue1) TextView mStepCountTv;

    private HealthDataStore mStore;
    //private StepCountReporter mReporter;
    BloodReporter mReporter;
    glucoseReporter gluecoseReporter;
    private Set<HealthPermissionManager.PermissionKey> mKeySet;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
        home=findViewById(R.id.home_txt);
        meal_txt=findViewById(R.id.meal_txt);
        pill_txt=findViewById(R.id.pill_txt);
        currentdate=findViewById(R.id.date);
        mKeySet = new HashSet<HealthPermissionManager.PermissionKey>();
        mKeySet.add(new PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE, PermissionType.READ));
        mKeySet.add(new PermissionKey(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, PermissionType.READ));
        mStore = new HealthDataStore(this, mConnectionListener);
        // Request the connection to the health data store
        mStore.connectService();
        final SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String date = sdf.format(calendar.getTime());
        wakesugartext=findViewById(R.id.wakesugartext);
        wakepressuretext=findViewById(R.id.wakepressureetext);
        wakeuptext=findViewById(R.id.wakeuptext);
        wakeupedit=findViewById(R.id.wakeupedit);
        mBloodSugar.wakeupread(date);
        mBloodSugar.morningread(date);
        mBloodSugar.lunchread(date);
        mBloodSugar.dinnerread(date);
        mBloodSugar.sleepread(date);
//        mBloodSugar.wakeupbloodPressure(date);
//        mBloodSugar.readwakeup(date);
      //  mBloodSugar.morning(date);
        mBloodSugar.mHandler1 = new Handler(){
            @Override public void handleMessage(Message msg){
                if (msg.what==1000){
                    wakesugartext.setText(mBloodSugar.getBloodsugar());
                    wakepressuretext.setText(mBloodSugar.getBloodpressure());
                    morningsugartext.setText(mBloodSugar.getmBloodsugar());
                    morningpressuretext.setText(mBloodSugar.getmBloodpressure());
                    lunchsugartext.setText(mBloodSugar.getLbloodsugar());
                    lunchpressuretext.setText(mBloodSugar.getLbloodpressure());
                    dinnersugartext.setText(mBloodSugar.getDbloodsugar());
                    dinnerpressuretext.setText(mBloodSugar.getDbloodpressure());
                    sleepsugartext.setText(mBloodSugar.getSbloodsugar());
                    sleeppressuretext.setText(mBloodSugar.getSbloodpressure());
                }
            }
        };

        currentdate.setText(date);

        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);

        //mBloodSugar.wakeupbloodSugar(mBloodSugar.getBloodpressure());
       // mBloodSugar.wakeupbloodPressure(mBloodSugar.getBloodsugar());


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 < 버튼 클릭
                SimpleDateFormat sdf2 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌.
                String yesterday = sdf.format(calendar.getTime());
                mBloodSugar.wakeupread(yesterday);
                mBloodSugar.morningread(yesterday);
                mBloodSugar.lunchread(yesterday);
                mBloodSugar.dinnerread(yesterday);
                mBloodSugar.sleepread(yesterday);
                //mBloodSugar.wakeupbloodPressure(yesterday);
               // mBloodSugar.readwakeup(yesterday);
                //mBloodSugar.morning(yesterday);
                currentdate.setText(yesterday);
                mBloodSugar.mHandler1 = new Handler(){
                    @Override public void handleMessage(Message msg){
                    if (msg.what==1000){
                            Log.d("메세지 받음",mBloodSugar.getBloodsugar());
                            wakesugartext.setText(mBloodSugar.getBloodsugar());
                            wakepressuretext.setText(mBloodSugar.getBloodpressure());
                        morningsugartext.setText(mBloodSugar.getmBloodsugar());
                        morningpressuretext.setText(mBloodSugar.getmBloodpressure());
                        lunchsugartext.setText(mBloodSugar.getLbloodsugar());
                        lunchpressuretext.setText(mBloodSugar.getLbloodpressure());
                        dinnersugartext.setText(mBloodSugar.getDbloodsugar());
                        dinnerpressuretext.setText(mBloodSugar.getDbloodpressure());
                        sleepsugartext.setText(mBloodSugar.getSbloodsugar());
                        sleeppressuretext.setText(mBloodSugar.getSbloodpressure());
                    }
                }
                };
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 > 버튼 클릭
                calendar.add(Calendar.DATE, +1);  // 오늘 날짜에서 하루를 더함
                String tomorrow = sdf.format(calendar.getTime());
                currentdate.setText(tomorrow);
              //  mBloodSugar.readwakeup(tomorrow);

                mBloodSugar.wakeupread(tomorrow);
                mBloodSugar.morningread(tomorrow);
                mBloodSugar.lunchread(tomorrow);
                mBloodSugar.dinnerread(tomorrow);
                mBloodSugar.sleepread(tomorrow);
//                mBloodSugar.wakeupbloodPressure(tomorrow);
            //    mBloodSugar.morning(tomorrow);
                mBloodSugar.mHandler1 = new Handler(){
                    @Override public void handleMessage(Message msg){
                        if (msg.what==1000){
                            Log.d("메세지 받음",mBloodSugar.getBloodsugar());
                            wakesugartext.setText(mBloodSugar.getBloodsugar());
                            wakepressuretext.setText(mBloodSugar.getBloodpressure());
                            morningsugartext.setText(mBloodSugar.getmBloodsugar());
                            morningpressuretext.setText(mBloodSugar.getmBloodpressure());
                            lunchsugartext.setText(mBloodSugar.getLbloodsugar());
                            lunchpressuretext.setText(mBloodSugar.getLbloodpressure());
                            dinnersugartext.setText(mBloodSugar.getDbloodsugar());
                            dinnerpressuretext.setText(mBloodSugar.getDbloodpressure());
                            sleepsugartext.setText(mBloodSugar.getSbloodsugar());
                            sleeppressuretext.setText(mBloodSugar.getSbloodpressure());
                        }
                    }
                };

            }
        });

//새로 추가 끝




        //기상 후
        wakeup=findViewById(R.id.wakeup);
        wakeUp=findViewById(R.id.wakeUp);
        wakeupbloodedit=(TextView)findViewById(R.id.wakeupbloodedit);

        wakeupbloodfinish=findViewById(R.id.wakeupbloodfinish);
        wakesugaredit=findViewById(R.id.wakesugaredit);
        wakepressureedit=findViewById(R.id.wakepressureedit);
        wakepressureedit2=findViewById(R.id.wakepressureedit2);

        //아침
        morning=findViewById(R.id.morning);
        Morning=findViewById(R.id.Morning);
        morningbloodedit=findViewById(R.id.morningbloodedit);
        morningsugartext=findViewById(R.id.morningsugartext);
        morningpressuretext=findViewById(R.id.morningpressuretext);
        morningbloodfinish=findViewById(R.id.morningbloodfinish);
        morningsugaredit=findViewById(R.id.morningsugaredit);
        morningpressureedit=findViewById(R.id.morningpressureedit);
        morningpressureedit2=findViewById(R.id.morningpressureedit2);
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

                Log.d(this.getClass().getName(),"연필클릭");
                wakeUp.setVisibility(View.GONE);

                wakeupbloodedit.setVisibility(View.GONE);
                wakesugartext.setVisibility(View.GONE);
                wakepressuretext.setVisibility(View.GONE);
                wakeupbloodfinish.setVisibility(View.VISIBLE);
                wakesugaredit.setVisibility(View.VISIBLE);
                wakepressureedit.setVisibility(View.VISIBLE);
                wakepressureedit2.setVisibility(View.VISIBLE);
                wakeuptext.setVisibility(View.GONE);
                wakeupedit.setVisibility(View.VISIBLE);
                wakeup.setVisibility(View.VISIBLE);

            }
        });

        wakeupbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 O
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                wakeup.setVisibility(View.GONE);
                wakeUp.setVisibility(View.VISIBLE);
                wakeupbloodedit.setVisibility(View.VISIBLE);
                wakesugartext.setVisibility(View.VISIBLE);
                wakepressuretext.setVisibility(View.VISIBLE);
                wakeupbloodfinish.setVisibility(View.GONE);
                wakesugaredit.setVisibility(View.GONE);
                wakepressureedit2.setVisibility(View.GONE);
                wakeuptext.setVisibility(View.VISIBLE);
                wakeupedit.setVisibility(View.GONE);
                wakepressureedit.setVisibility(View.GONE);
                String wakesugar=(String)wakesugaredit.getText().toString();
                wakesugartext.setText(wakesugar);

                String wakepressure = (String)wakepressureedit.getText().toString();
                wakepressuretext.setText(wakepressure);

                /*
                bloodsugarDB : userid - { bs, bp, date, time }
                 */
                // DB 삽입부
                time = wakeup.toString();
                time = "기상 후";
                Log.d(this.getClass().getName(),wakesugar+"이거닷~!!!!!!1");
                mBloodSugar.create(Double.parseDouble(wakesugar), Double.parseDouble(wakepressure),date, time);
            }

        });
        morningbloodedit.setOnClickListener(new View.OnClickListener() { // 아침 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Morning.setVisibility(View.GONE);
                morningbloodedit.setVisibility(View.GONE);
                morningsugartext.setVisibility(View.GONE);
                morningpressuretext.setVisibility(View.GONE);
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
                morning.setVisibility(View.GONE);
                Morning.setVisibility(View.VISIBLE);
                morningbloodedit.setVisibility(View.VISIBLE);
                morningsugartext.setVisibility(View.VISIBLE);
                morningpressuretext.setVisibility(View.VISIBLE);
                morningbloodfinish.setVisibility(View.GONE);
                morningsugaredit.setVisibility(View.GONE);
                morningpressureedit.setVisibility(View.GONE);
                String morningsugar = (String) morningsugaredit.getText().toString();
                morningsugartext.setText(morningsugar); // 입력한 아침 혈당을 morningsugartext에 입력

                String morningpressure = (String)morningpressureedit.getText().toString();
                morningpressuretext.setText(morningpressure); // 입력한 아침 혈압을 morningpressuretext에 입력

                time = morning.toString();

                time = "아침";
                mBloodSugar.create(Double.parseDouble(morningsugar), Double.parseDouble(morningpressure), date, time);

                Log.d(this.getClass().getName(),morningsugar+"아침혈당이거닷~!!!!!!1");

            }

        });


        lunchbloodedit.setOnClickListener(new View.OnClickListener() { // 점심 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Lunch.setVisibility(View.GONE);
                lunchbloodedit.setVisibility(View.GONE);
                lunchsugartext.setVisibility(View.GONE);
                lunchpressuretext.setVisibility(View.GONE);
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
                lunch.setVisibility(View.GONE);
                Lunch.setVisibility(View.VISIBLE);
                lunchbloodedit.setVisibility(View.VISIBLE);
                lunchsugartext.setVisibility(View.VISIBLE);
                lunchpressuretext.setVisibility(View.VISIBLE);
                lunchbloodfinish.setVisibility(View.GONE);
                lunchsugaredit.setVisibility(View.GONE);
                lunchpressureedit.setVisibility(View.GONE);
                String lunchsugar = (String)lunchsugaredit.getText().toString(); // 입력한 점심 혈당을 lunchsugaredit에 입력
                lunchsugartext.setText(lunchsugar);

                String lunchpressure = (String)lunchpressureedit.getText().toString(); // 입력한 점심 혈압을 lunchpressureedit에 입력
                lunchpressuretext.setText(lunchpressure);


                time = "점심";

                Log.d(this.getClass().getName(),lunchsugar+"점심혈당이거닷~!!!!!!1");
             //   mBloodSugar.create("userid", Double.parseDouble(lunchsugar), Double.parseDouble(lunchpressure), new Date(), time);
                  mBloodSugar.create(Double.parseDouble(lunchsugar), Double.parseDouble(lunchpressure), date, time);

            }

        });

        dinnerbloodedit.setOnClickListener(new View.OnClickListener() { // 저녁 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Dinner.setVisibility(View.GONE);
                dinnerbloodedit.setVisibility(View.GONE);
                dinnersugartext.setVisibility(View.GONE);
                dinnerpressuretext.setVisibility(View.GONE);
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
                dinner.setVisibility(View.GONE);
                Dinner.setVisibility(View.VISIBLE);
                dinnerbloodedit.setVisibility(View.VISIBLE);
                dinnersugartext.setVisibility(View.VISIBLE);
                dinnerpressuretext.setVisibility(View.VISIBLE);
                dinnerbloodfinish.setVisibility(View.GONE);
                dinnersugaredit.setVisibility(View.GONE);
                dinnerpressureedit.setVisibility(View.GONE);
                String dinnersugar = (String)dinnersugaredit.getText().toString(); // 입력한 저녁 혈당을 lunchsugaredit에 입력
                dinnersugartext.setText(dinnersugar);

                String dinnerpressure = (String)dinnerpressureedit.getText().toString(); // 입력한 저녁 혈압을 lunchpressureedit에 입력
                dinnerpressuretext.setText(dinnerpressure);


                time = "저녁";

                Log.d(this.getClass().getName(),dinnersugar+"저녁혈당이거닷~!!!!!!1");
             //   mBloodSugar.create("userid", Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure), new Date(), time);
                   mBloodSugar.create(Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure), date, time);

            }

        });

        sleepbloodedit.setOnClickListener(new View.OnClickListener() { // 취침 후  행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Sleep.setVisibility(View.GONE);
                sleepbloodedit.setVisibility(View.GONE);
                sleepsugartext.setVisibility(View.GONE);
                sleeppressuretext.setVisibility(View.GONE);
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
                sleep.setVisibility(View.GONE);
                Sleep.setVisibility(View.VISIBLE);
                sleepbloodedit.setVisibility(View.VISIBLE);
                sleepsugartext.setVisibility(View.VISIBLE);
                sleeppressuretext.setVisibility(View.VISIBLE);
                sleepbloodfinish.setVisibility(View.GONE);
                sleepsugaredit.setVisibility(View.GONE);
                sleeppressureedit.setVisibility(View.GONE);
        String sleepsugar = (String)sleepsugaredit.getText().toString(); // 입력한 취침 후  혈당을 dinnersugaredit에 입력
        sleepsugartext.setText(sleepsugar);

        String sleeppressure = (String)sleeppressureedit.getText().toString(); // 입력한 취침 후  혈압을 dinnerpressureedit에 입력
        sleeppressuretext.setText(sleeppressure);


                time = "취침 전";
                Log.d(this.getClass().getName(),sleepsugar+"취침혈당이거닷~!!!!!!1");
              //  mBloodSugar.create("userid", Double.parseDouble(sleepsugar), Double.parseDouble(sleeppressure), new Date(), time);
                 mBloodSugar.create(Double.parseDouble(sleepsugar), Double.parseDouble(sleeppressure), date, time);

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
    @Override
    public void onDestroy() {
        mStore.disconnectService();
        super.onDestroy();
    }
    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            mReporter = new BloodReporter(mStore);
            gluecoseReporter=new glucoseReporter(mStore);
            if (isPermissionAcquired()) {
                mReporter.start(mStepCountObserver);
                gluecoseReporter.start(mStepCountObserver2);
            } else {
                requestPermission();
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");
            if (!isFinishing()) {
                mStore.connectService();
            }
        }
    };

    private void showPermissionAlarmDialog() {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(bloodActivity.this);
        alert.setTitle(R.string.notice)
                .setMessage(R.string.msg_perm_acquired)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (error.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    alert.setMessage(R.string.msg_req_install);
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    alert.setMessage(R.string.msg_req_upgrade);
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    alert.setMessage(R.string.msg_req_enable);
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    alert.setMessage(R.string.msg_req_agree);
                    break;
                default:
                    alert.setMessage(R.string.msg_req_available);
                    break;
            }
        } else {
            alert.setMessage(R.string.msg_conn_not_available);
        }

        alert.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (error.hasResolution()) {
                error.resolve(bloodActivity.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }

    public boolean isPermissionAcquired() {
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(mKeySet);
            return resultMap.get(mKeySet);
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission request fails.", e);
        }
        return false;
    }



    private void requestPermission() {
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(mKeySet, bloodActivity.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {
                            // Requesting permission fails
                            updateStepCountView("");
                            showPermissionAlarmDialog();
                        } else {
                            gluecoseReporter.start(mStepCountObserver2);
                            mReporter.start(mStepCountObserver);
                        }
                    });
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission setting fails.", e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    private BloodReporter.BloodObserver mStepCountObserver = new BloodReporter.BloodObserver() {

        @Override
        public void onChanged(String count) {
            Log.d(APP_TAG, "Step reported : " + count);
            bloodActivity.this.updateStepCountView(String.valueOf(count));
        }
    };

    public HealthDataStore.ConnectionListener getmConnectionListener() {
        return mConnectionListener;
    }

    private glucoseReporter.BloodglucoseObserver mStepCountObserver2 = new glucoseReporter.BloodglucoseObserver() {

        @Override
        public void onChanged(String count) {
            Log.d(APP_TAG, "Step reported : " + count);
            bloodActivity.this.updateStepCountView(String.valueOf(count));
        }
    };
    private void updateStepCountView(final String count) {
        Log.d("읽어온 혈당값",count);

    }


//    @Override
//    public boolean onOptionsItemSelected(android.view.MenuItem item) {
//
//        if (item.getItemId() == R.id.connect) {
//            requestPermission();
//        }
//
//        return true;
//    }


}