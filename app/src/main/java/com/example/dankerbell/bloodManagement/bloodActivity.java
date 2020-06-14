package com.example.dankerbell.bloodManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.LoginInActivity;
import com.example.dankerbell.ProfileActivity;
import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.BloodReporter;
import com.example.dankerbell.bloodManagement.glucoseReporter;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.myprofileActivity;
import com.example.dankerbell.mysetTimeActivity;
import com.example.dankerbell.pillManagement.pillActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;


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
    private GoogleSignInClient mGoogleSignInClient;

    TextView toolbar;
    TextView close;
    DrawerLayout drawerLayout;
    View drawerView;
    Button mypage;
    Button logout,settime;
    TextView userid;

    TextView home; //
    TextView meal_txt; // 상단에 식단관리 TextView
    TextView pill_txt; // 상단에 복약관리 TextView
        TextView wakeup,wakeUp,wakeupbloodedit,wakesugartext,wakeupbloodfinish;  // 기상 후 !!!
    EditText inputdang,inputcol;
    // wakeUp,wakeup=기상후 , wakeupbloodedit : 기상 후 연필 아이콘 , wakesugartext : 기상 후 혈당 text wakepressuretext : 기상 후 혈압text
    // wakepressuretext : 기상 후 혈압 text wakeupbloodfinish : 체크 아이콘

    //---인슐린
    TextView wakeregularinsulintext,wakeNPHinsulintext,wakeUltrainsulintext;
    TextView morningregularinsulintext,morningNPHinsulintext,morningUltrainsulintext;
    TextView lunchregularinsulintext,lunchNPHinsulintext,lunchUltrainsulintext;
    TextView dinnerregularinsulintext,dinnerNPHinsulintext,dinnerUltrainsulintext;
    TextView sleepregularinsulintext,sleepNPHinsulintext,sleepUltrainsulintext;

    EditText wakeregularinsulinedit,wakeNPHinsulinedit,wakeUltrainsulinedit;
    EditText morningregularinsulinedit,morningNPHinsulinedit,morningUltrainsulinedit;
    EditText lunchregularinsulinedit,lunchNPHinsulinedit,lunchUltrainsulinedit;
    EditText dinnerregularinsulinedit,dinnerNPHinsulinedit,dinnerUltrainsulinedit;
    EditText sleepregularinsulinedit,sleepNPHinsulinedit,sleepUltrainsulinedit;

    EditText wakesugaredit; // 기상 후 혈당입력칸 / 혈압 입력칸
    TextView Morning,morning,morningbloodedit,morningsugartext,morningbloodfinish;  // 아침 !!!
    EditText morningsugaredit; // 아침 혈당입력칸 / 혈압 입력칸
    TextView Lunch,lunch,lunchbloodedit,lunchsugartext,lunchbloodfinish;  // 점심 !!!
    // wakeUp,wakeup=기상후 , wakeupbloodedit : 기상 후 연필 아이콘 , wakesugartext : 기상 후 혈당 text wakepressuretext : 기상 후 혈압text
    // wakepressuretext : 기상 후 혈압 text wakeupbloodfinish : 체크 아이콘


    // Lunch,lunch : 점심 , lunchbloodedit : 점심 연필 아이콘 , lunchsugartext : 점심 혈당 text, lunchbloodfinish : 점심 혈압 text
    // lunchpressuretext : 점심 혈압 text lunchbloodfinish : 체크 아이콘
    EditText lunchsugaredit; // 점심 혈당입력칸 / 혈압 입력칸

    public static String getDate() {
        return date;
    }

    TextView Dinner,dinner,dinnerbloodedit,dinnersugartext, dinnerbloodfinish;  // 저녁 !!!
    EditText dinnersugaredit; // 저녁 혈당입력칸 / 혈압 입력칸

    TextView Sleep,sleep,sleepbloodedit,sleepsugartext,sleepbloodfinish;  // 취침전 !!!
    EditText sleepsugaredit; // 취침 전 혈당입력칸 / 혈압 입력칸



    GridLayout morningtextgrid,morningeditgrid,lunchtextgrid,luncheditgrid,dinnertextgrid,dinnereditgrid,sleeptextgrid,sleepeditgrid,waketextgrid,wakeeditgrid;

    TextView currentdate;
    TextView prev,next;
    private static Handler mHandler ;
    String time; // 기상후, 식전 식후 등

    TextView textdangwha,textcol,editcol,finishcol;
    Button drawer_pill,drawer_meal,drawer_blood;


    static String date="";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
        cholesterolCrud mchol=cholesterolCrud.getInstance();
        home=findViewById(R.id.home_txt);
        meal_txt=findViewById(R.id.meal_txt);
        pill_txt=findViewById(R.id.pill_txt);
        currentdate=findViewById(R.id.date);
       userid=findViewById(R.id.userid); // !!!!!!!
        inputdang=findViewById(R.id.inputdangwha);
        textdangwha=findViewById(R.id.textdangwha);
        textcol=findViewById(R.id.textcol);
        editcol=findViewById(R.id.editcol);
        finishcol=findViewById(R.id.finishcol);
        inputcol=findViewById(R.id.inputcol);
        drawer_meal=findViewById(R.id.drawer_meal);
        drawer_pill=findViewById(R.id.drawer_pill);
        settime=findViewById(R.id.settime);

        userid=findViewById(R.id.userid); // !!!!!!!
        userid.setText(User);


        final SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        final SimpleDateFormat timeinclude = new SimpleDateFormat("yy.MM.dd.HH:MM", Locale.getDefault());
        SimpleDateFormat monthformat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat monthofdayformat = new SimpleDateFormat("dd", Locale.getDefault());

        final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String day = monthofdayformat.format(calendar.getTime());
        String month=monthformat.format(calendar.getTime());

        date = sdf.format(calendar.getTime());
        final String timeminutedate=timeinclude.format(calendar.getTime());


        final String date = sdf.format(calendar.getTime());
        wakesugartext=findViewById(R.id.wakesugartext);
        userid.setText(User);

        mBloodSugar.wakeupread(month,day); //!!!!!!1
        mBloodSugar.morningread(month,day);
        mBloodSugar.lunchread(month,day);
        mBloodSugar.dinnerread(month,day);
        mBloodSugar.sleepread(month,day);

        toolbar=findViewById(R.id.toolbar_menu);
        drawerLayout=findViewById(R.id.drawer_layout) ;
        mypage = findViewById(R.id.mypage);
        logout = findViewById(R.id.logout);
        drawerView=findViewById(R.id.drawer);
        close=findViewById(R.id.toolbar_close);

        drawer_blood=findViewById(R.id.drawer_blood);
        drawer_meal=findViewById(R.id.drawer_meal);
        drawer_pill=findViewById(R.id.drawer_pill);
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setTimeintent = new Intent(getApplicationContext(), mysetTimeActivity.class);
                startActivity(setTimeintent);//액티비티 띄우기
            }
        });
        drawer_pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pill = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pill);//혈당관리 클래스 전환
            }
        });
        drawer_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(blood);//혈당관리 클래스 전환
            }
        });
        drawer_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meal = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(meal);//식단관리 클래스 전환
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent drawer = new Intent(getApplicationContext(), DrawerActivity.class);
//                startActivity(drawer);//액티비티 띄우기
                drawerLayout.openDrawer(drawerView);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
            }
        });
        mypage.setOnClickListener(new View.OnClickListener() { // 내 정보 버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), myprofileActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        logout.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭

            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"로그아웃 클릭");
                signOut();
            }
        });
        mchol.read(date);
        mchol.cholHandler = new Handler(){
                    @Override public void handleMessage(Message msg){
                        if (msg.what==1001){
                            Log.d(this.getClass().getName(),"콜레스테롤 메세지 받음");
                            Log.d(this.getClass().getName(),mchol.getCholesterol());

                            textcol.setText(mchol.getCholesterol());
                            textdangwha.setText(mchol.getDangwha());
                        }
                    }

                };

        mBloodSugar.mHandler1 = new Handler(){
            @Override public void handleMessage(Message msg){
                if (msg.what==1000){
                    Log.d("혈당,인슐린 메세지","메세지 받음");
                    wakesugartext.setText(mBloodSugar.getBloodsugar());
                    wakeregularinsulintext.setText(mBloodSugar.getWregular());
                    wakeNPHinsulintext.setText(mBloodSugar.getwNPH());
                    wakeUltrainsulintext.setText(mBloodSugar.getWultra());
                    morningsugartext.setText(mBloodSugar.getMbloodsugar());
                    morningregularinsulintext.setText(mBloodSugar.getMregular());
                    morningNPHinsulintext.setText(mBloodSugar.getmNPH());
                    morningUltrainsulintext.setText(mBloodSugar.getMultra());

                    lunchsugartext.setText(mBloodSugar.getlbloodsugar());
                    lunchregularinsulintext.setText(mBloodSugar.getLregular());
                    lunchNPHinsulintext.setText(mBloodSugar.getlNPH());
                    lunchUltrainsulintext.setText(mBloodSugar.getLultra());

                    dinnersugartext.setText(mBloodSugar.getdbloodsugar());
                    dinnerregularinsulintext.setText(mBloodSugar.getDregular());
                    dinnerNPHinsulintext.setText(mBloodSugar.getdNPH());
                    dinnerUltrainsulintext.setText(mBloodSugar.getDultra());

                    sleepsugartext.setText(mBloodSugar.getsbloodsugar());
                    sleepregularinsulintext.setText(mBloodSugar.getSregular());
                    sleepNPHinsulintext.setText(mBloodSugar.getsNPH());
                    sleepUltrainsulintext.setText(mBloodSugar.getSultra());

                    morningsugartext.setText(mBloodSugar.getMbloodsugar());
                    lunchsugartext.setText(mBloodSugar.getlbloodsugar());
                    dinnersugartext.setText(mBloodSugar.getdbloodsugar());
                    sleepsugartext.setText(mBloodSugar.getsbloodsugar());


                }
            }
        };

        currentdate.setText(date);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 < 버튼 클릭
                SimpleDateFormat sdf2 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌.
                String yesterday = sdf.format(calendar.getTime());
                String yesterdaymonth = monthformat.format(calendar.getTime());
                String yesterdayday = monthofdayformat.format(calendar.getTime());

                mBloodSugar.wakeupread(yesterdaymonth,yesterdayday);
                mBloodSugar.morningread(yesterdaymonth,yesterdayday);
                mBloodSugar.lunchread(yesterdaymonth,yesterdayday);
                mBloodSugar.dinnerread(yesterdaymonth,yesterdayday);
                mBloodSugar.sleepread(yesterdaymonth,yesterdayday);
                mchol.read(yesterday);
                mchol.cholHandler = new Handler(){

                    @Override public void handleMessage(Message msg){
                        if (msg.what==1001){
                            textcol.setText(mchol.getCholesterol());
                            textdangwha.setText(mchol.getDangwha());
                        }
                    }

                };

                currentdate.setText(yesterday);
                mBloodSugar.mHandler1 = new Handler(){
                    @Override public void handleMessage(Message msg){
                    if (msg.what==1000){
                            Log.d("메세지 받음",mBloodSugar.getBloodsugar());
                            wakesugartext.setText(mBloodSugar.getBloodsugar());
                            wakeregularinsulintext.setText(mBloodSugar.getWregular());
                            wakeNPHinsulintext.setText(mBloodSugar.getwNPH());
                            wakeUltrainsulintext.setText(mBloodSugar.getWultra());

                            morningsugartext.setText(mBloodSugar.getMbloodsugar());
                            morningregularinsulintext.setText(mBloodSugar.getMregular());
                            morningNPHinsulintext.setText(mBloodSugar.getmNPH());
                            morningUltrainsulintext.setText(mBloodSugar.getMultra());

                            lunchsugartext.setText(mBloodSugar.getlbloodsugar());
                            lunchregularinsulintext.setText(mBloodSugar.getLregular());
                            lunchNPHinsulintext.setText(mBloodSugar.getlNPH());
                            lunchUltrainsulintext.setText(mBloodSugar.getLultra());

                            dinnersugartext.setText(mBloodSugar.getdbloodsugar());
                            dinnerregularinsulintext.setText(mBloodSugar.getDregular());
                            dinnerNPHinsulintext.setText(mBloodSugar.getdNPH());
                            dinnerUltrainsulintext.setText(mBloodSugar.getDultra());

                            sleepsugartext.setText(mBloodSugar.getsbloodsugar());
                            sleepregularinsulintext.setText(mBloodSugar.getSregular());
                            sleepNPHinsulintext.setText(mBloodSugar.getsNPH());
                            sleepUltrainsulintext.setText(mBloodSugar.getSultra());
                        }
                    }

                };
            }
        });
        drawer_pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pill = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pill);//혈당관리 클래스 전환
            }
        });
//        drawer_blood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent blood = new Intent(getApplicationContext(), bloodActivity.class);
//                startActivity(blood);//혈당관리 클래스 전환
//            }
//        });
        drawer_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meal = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(meal);//식단관리 클래스 전환
            }
        });
        finishcol.setOnClickListener(new View.OnClickListener() { //당화혈 체크 클릭
            @Override
            public void onClick(View view) {
                textcol.setText(inputcol.getText());
                textdangwha.setText(inputdang.getText());
                inputcol.setVisibility(View.GONE);
                inputdang.setVisibility(View.GONE);
                textcol.setVisibility(View.VISIBLE);
                textdangwha.setVisibility(View.VISIBLE);
                editcol.setVisibility(View.VISIBLE);
                finishcol.setVisibility(View.GONE);
                String col=inputcol.getText().toString();
                String dang=inputdang.getText().toString();
                if(col.equals("")|dang.equals("")){
                    notifyshow();
                }
                else{
                    mchol.create(col,dang,date);
                    //finishshow();

                }
            }
        });
        editcol.setOnClickListener(new View.OnClickListener() { //당화혈 수정 클릭
            @Override
            public void onClick(View view) {
                textcol.setVisibility(View.GONE);
                textdangwha.setVisibility(View.GONE);
                inputcol.setVisibility(View.VISIBLE);
                inputdang.setVisibility(View.VISIBLE);
                editcol.setVisibility(View.GONE);
                finishcol.setVisibility(View.VISIBLE);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 > 버튼 클릭
                calendar.add(Calendar.DATE, +1);  // 오늘 날짜에서 하루를 더함
                String tomorrow = sdf.format(calendar.getTime());
                String tomorrowmonth = monthformat.format(calendar.getTime());
                String tomorrowday = monthofdayformat.format(calendar.getTime());

                currentdate.setText(tomorrow);

                mchol.read(tomorrow); // !!!!!!!!!!
                mBloodSugar.wakeupread(tomorrowmonth,tomorrowday);
                mBloodSugar.morningread(tomorrowmonth,tomorrowday);
                mBloodSugar.lunchread(tomorrowmonth,tomorrowday);
                mBloodSugar.dinnerread(tomorrowmonth,tomorrowday);
                mBloodSugar.sleepread(tomorrowmonth,tomorrowday);

                       mchol.cholHandler = new Handler(){

                    @Override public void handleMessage(Message msg){
                        if (msg.what==1001){
                            textcol.setText(mchol.getCholesterol());
                            textdangwha.setText(mchol.getDangwha());
                        }
                    }

                };

                mBloodSugar.mHandler1 = new Handler(){
                    @Override public void handleMessage(Message msg){
                        if (msg.what==1000){
                            Log.d("메세지 받음",mBloodSugar.getBloodsugar());
                            wakesugartext.setText(mBloodSugar.getBloodsugar());
                            wakeregularinsulintext.setText(mBloodSugar.getWregular());
                            wakeNPHinsulintext.setText(mBloodSugar.getwNPH());
                            wakeUltrainsulintext.setText(mBloodSugar.getWultra());

                            morningsugartext.setText(mBloodSugar.getMbloodsugar());
                            morningregularinsulintext.setText(mBloodSugar.getMregular());
                            morningNPHinsulintext.setText(mBloodSugar.getmNPH());
                            morningUltrainsulintext.setText(mBloodSugar.getMultra());

                            lunchsugartext.setText(mBloodSugar.getlbloodsugar());
                            lunchregularinsulintext.setText(mBloodSugar.getLregular());
                            lunchNPHinsulintext.setText(mBloodSugar.getlNPH());
                            lunchUltrainsulintext.setText(mBloodSugar.getLultra());

                            dinnersugartext.setText(mBloodSugar.getdbloodsugar());
                            dinnerregularinsulintext.setText(mBloodSugar.getDregular());
                            dinnerNPHinsulintext.setText(mBloodSugar.getdNPH());
                            dinnerUltrainsulintext.setText(mBloodSugar.getDultra());

                            sleepsugartext.setText(mBloodSugar.getsbloodsugar());
                            sleepregularinsulintext.setText(mBloodSugar.getSregular());
                            sleepNPHinsulintext.setText(mBloodSugar.getsNPH());
                            sleepUltrainsulintext.setText(mBloodSugar.getSultra());
                        }
                    }
                };

            }
        });

        morningeditgrid=findViewById(R.id.morningeditgrid);
        morningtextgrid=findViewById(R.id.morningtextgrid);
        waketextgrid=findViewById(R.id.waketextgrid);
        wakeeditgrid=findViewById(R.id.wakeeditgrid);
        lunchtextgrid=findViewById(R.id.lunchtextgrid);
        luncheditgrid=findViewById(R.id.luncheditgrid);
        dinnertextgrid=findViewById(R.id.dinnertextgrid);
        dinnereditgrid=findViewById(R.id.dinnereditgrid);
        sleeptextgrid=findViewById(R.id.sleeptextgrid);
        sleepeditgrid=findViewById(R.id.sleepeditgrid);


        //기상 후
        wakeup=findViewById(R.id.wakeup);
        wakeUp=findViewById(R.id.wakeUp);
        wakeregularinsulintext=findViewById(R.id.wakereinsulintext);
        wakeNPHinsulintext=findViewById(R.id.wakeNPHinsulintext);
        wakeUltrainsulintext=findViewById(R.id.wakeUltrainsulintext);
        wakeupbloodedit=(TextView)findViewById(R.id.wakeupbloodedit);
        wakeupbloodfinish=findViewById(R.id.wakeupbloodfinish);
        wakesugaredit=findViewById(R.id.wakesugaredit);
        wakeregularinsulinedit=findViewById(R.id.wakeregularinsulinedit);
        wakeNPHinsulinedit=findViewById(R.id.wakeNPHinsulinedit);
        wakeUltrainsulinedit=findViewById(R.id.wakeUltrainsulinedit);
        //아침
        morning=findViewById(R.id.morning);
        Morning=findViewById(R.id.Morning);
        morningbloodedit=findViewById(R.id.morningbloodedit);
        morningsugartext=findViewById(R.id.morningsugartext);
        morningregularinsulintext=findViewById(R.id.morningregularinsulintext);
        morningNPHinsulintext=findViewById(R.id.morningNPHinsulintext);
        morningUltrainsulintext=findViewById(R.id.morningUltrainsulintext);
        morningregularinsulinedit=findViewById(R.id.morningregularinsulinedit);
        morningNPHinsulinedit=findViewById(R.id.morningNPHinsulinedit);
        morningUltrainsulinedit=findViewById(R.id.morningUltrainsulinedit);

        morningbloodfinish=findViewById(R.id.morningbloodfinish);
        morningsugaredit=findViewById(R.id.morningsugaredit);

        //점심
        lunch=findViewById(R.id.lunch);
        Lunch=findViewById(R.id.Lunch);
        lunchbloodedit=findViewById(R.id.lunchbloodedit);
        lunchsugartext=findViewById(R.id.lunchsugartext);
        lunchregularinsulintext=findViewById(R.id.lunchregularinsulintext);
        lunchNPHinsulintext=findViewById(R.id.lunchNPHinsulintext);;
        lunchUltrainsulintext=findViewById(R.id.lunchUltrainsulintext);
        lunchregularinsulinedit=findViewById(R.id.lunchregularinsulinedit);
        lunchNPHinsulinedit=findViewById(R.id.lunchNPHinsulinedit);
        lunchUltrainsulinedit=findViewById(R.id.lunchUltrainsulinedit);
        lunchbloodfinish=findViewById(R.id.lunchbloodfinish);
        lunchsugaredit=findViewById(R.id.lunchsugaredit);

        //저녁
        dinner=findViewById(R.id.dinner);
        Dinner=findViewById(R.id.Dinner);
        dinnerregularinsulintext=findViewById(R.id.dinnerregularinsulintext);
        dinnerNPHinsulintext=findViewById(R.id.dinnerNPHinsulintext);
        dinnerUltrainsulintext=findViewById(R.id.dinnerUltrainsulintext);
        dinnerregularinsulinedit=findViewById(R.id.dinnerregularinsulinedit);
        dinnerNPHinsulinedit=findViewById(R.id.dinnerNPHinsulinedit);
        dinnerUltrainsulinedit=findViewById(R.id.dinnerUltrainsulinedit);
        dinnerbloodedit=findViewById(R.id.dinnerbloodedit);
        dinnersugartext=findViewById(R.id.dinnersugartext);
        dinnerbloodfinish=findViewById(R.id.dinnerbloodfinish);
        dinnersugaredit=findViewById(R.id.dinnersugaredit);

        //취침 후
        sleep=findViewById(R.id.sleep);
        Sleep=findViewById(R.id.Sleep);
        sleepregularinsulintext=findViewById(R.id.sleepregualrinsulintext);
        sleepNPHinsulintext=findViewById(R.id.sleepNPHinsulintext);
        sleepUltrainsulintext=findViewById(R.id.sleepUltrainsulintext);
        sleepregularinsulinedit=findViewById(R.id.sleepregularinsulinedit);
        sleepNPHinsulinedit=findViewById(R.id.sleepNPHinsulinedit);
        sleepUltrainsulinedit=findViewById(R.id.sleepUltrainsulinedit);
        sleepbloodedit=findViewById(R.id.sleepbloodedit);
        sleepsugartext=findViewById(R.id.sleepsugartext);
        sleepbloodfinish=findViewById(R.id.sleepbloodfinish);
        sleepsugaredit=findViewById(R.id.sleepsugaredit);

        wakeupbloodedit.setOnClickListener(new View.OnClickListener() { // 기상 후 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {

                Log.d(this.getClass().getName(),"연필클릭");
                wakeUp.setVisibility(View.GONE);

                wakeupbloodedit.setVisibility(View.GONE);
                wakesugartext.setVisibility(View.GONE);
                waketextgrid.setVisibility(View.GONE);
                wakeeditgrid.setVisibility(View.VISIBLE);
                wakeupbloodfinish.setVisibility(View.VISIBLE);
                wakesugaredit.setVisibility(View.VISIBLE);
                wakeNPHinsulintext.setVisibility(View.GONE);
                wakeregularinsulintext.setVisibility(View.GONE);
                wakeUltrainsulintext.setVisibility(View.GONE);
                wakeNPHinsulinedit.setVisibility(View.VISIBLE);
                wakeregularinsulinedit.setVisibility(View.VISIBLE);
                wakeUltrainsulinedit.setVisibility(View.VISIBLE);
               // wakeupedit.setVisibility(View.VISIBLE);
                wakeup.setVisibility(View.VISIBLE);

            }
        });

        wakeupbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 O
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                waketextgrid.setVisibility(View.VISIBLE);
                wakeeditgrid.setVisibility(View.GONE);
                wakeup.setVisibility(View.GONE);
                wakeUp.setVisibility(View.VISIBLE);
                wakeupbloodedit.setVisibility(View.VISIBLE);
                wakesugartext.setVisibility(View.VISIBLE);
                wakeNPHinsulintext.setVisibility(View.VISIBLE);
                wakeregularinsulintext.setVisibility(View.VISIBLE);
                wakeUltrainsulintext.setVisibility(View.VISIBLE);
                wakeNPHinsulinedit.setVisibility(View.GONE);
                wakeregularinsulinedit.setVisibility(View.GONE);
                wakeUltrainsulinedit.setVisibility(View.GONE);
                wakeupbloodfinish.setVisibility(View.GONE);
                wakesugaredit.setVisibility(View.GONE);

                String wakesugar=(String)wakesugaredit.getText().toString();
                wakesugartext.setText(wakesugar);
                String wakeUltra = (String)wakeUltrainsulinedit.getText().toString(); //수축기
                String wakeregular = (String)wakeregularinsulinedit.getText().toString(); //수축기
                String wakeNPH = (String)wakeNPHinsulinedit.getText().toString(); //수축기

                wakeregularinsulintext.setText(wakeregular);
                wakeNPHinsulintext.setText(wakeNPH);
                wakeUltrainsulintext.setText(wakeUltra);

                /*
                bloodsugarDB : userid - { bs, bp, date, time }
                 */
                // DB 삽입부
                time = wakeup.toString();
                time = "기상후";
                Log.d(this.getClass().getName(),wakeNPH+"이거닷~!!!!!!1");
                if(wakesugar.equals("")|wakeregular.equals("")|wakeNPH.equals("")|wakeUltra.equals("")){
                    notifyshow();
                }
                else{
                    mBloodSugar.create(Double.parseDouble(wakesugar), Double.parseDouble(wakeregular),Double.parseDouble(wakeNPH),Double.parseDouble(wakeUltra),timeminutedate, time);
                }
             //  mBloodSugar.create(Double.parseDouble(wakesugar), Double.parseDouble(wakepressure1),Double.parseDouble(wakepressure2),date, time)
            }

        });
        morningbloodedit.setOnClickListener(new View.OnClickListener() { // 아침 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Morning.setVisibility(View.GONE);
                morningbloodedit.setVisibility(View.GONE);
                morningsugartext.setVisibility(View.GONE);
                morningtextgrid.setVisibility(View.GONE);
                morningeditgrid.setVisibility(View.VISIBLE);
                morningregularinsulintext.setVisibility(View.GONE);
                morningNPHinsulintext.setVisibility(View.GONE);
                morningUltrainsulintext.setVisibility(View.GONE);

                morningregularinsulinedit.setVisibility(View.VISIBLE);
                morningNPHinsulinedit.setVisibility(View.VISIBLE);
                morningUltrainsulinedit.setVisibility(View.VISIBLE);

                morningbloodfinish.setVisibility(View.VISIBLE);
                morningsugaredit.setVisibility(View.VISIBLE);
                morning.setVisibility(View.VISIBLE);

                morningbloodfinish.setVisibility(View.VISIBLE);
                morningsugaredit.setVisibility(View.VISIBLE);
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
                morningtextgrid.setVisibility(View.VISIBLE);
                morningeditgrid.setVisibility(View.GONE);
                morningbloodfinish.setVisibility(View.GONE);
                morningsugaredit.setVisibility(View.GONE);
                morningregularinsulintext.setVisibility(View.VISIBLE);
                morningNPHinsulintext.setVisibility(View.VISIBLE);
                morningUltrainsulintext.setVisibility(View.VISIBLE);
                morningregularinsulinedit.setVisibility(View.GONE);
                morningNPHinsulinedit.setVisibility(View.GONE);
                morningUltrainsulinedit.setVisibility(View.GONE);

                morningbloodedit.setVisibility(View.VISIBLE);
                morningsugartext.setVisibility(View.VISIBLE);

                morningbloodfinish.setVisibility(View.GONE);
                morningsugaredit.setVisibility(View.GONE);

                String morningsugar = (String) morningsugaredit.getText().toString();
                morningsugartext.setText(morningsugar); // 입력한 아침 혈당을 morningsugartext에 입력

                String mregular = (String)morningregularinsulinedit.getText().toString();
                morningregularinsulintext.setText(mregular); // 입력한 아침 인슐린 입력

                String mNPH = (String)morningNPHinsulinedit.getText().toString();
                morningNPHinsulintext.setText(mNPH); // 입력한 아침 혈압을 morningpressuretext에 입력

                String mUltra = (String)morningUltrainsulinedit.getText().toString();
                morningUltrainsulintext.setText(mUltra); // 입력한 아침 혈압을 morningpressuretext에 입력


                time = morning.toString();
                time = "아침";
                //mBloodSugar.create(Double.parseDouble(morningsugar), Double.parseDouble(morningpressure),Double.parseDouble(morningpressure2), date, time);
                if(morningsugar.equals("")|mregular.equals("")|mNPH.equals("")|mUltra.equals("")){
                    notifyshow();
                }
                else{
                    mBloodSugar.create(Double.parseDouble(morningsugar), Double.parseDouble(mregular),Double.parseDouble(mNPH),Double.parseDouble(mUltra) ,timeminutedate, time);

                }
            }

        });


        lunchbloodedit.setOnClickListener(new View.OnClickListener() { // 점심 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Lunch.setVisibility(View.GONE);
                lunchbloodedit.setVisibility(View.GONE);
                lunchsugartext.setVisibility(View.GONE);
                lunchbloodfinish.setVisibility(View.VISIBLE);
                lunchsugaredit.setVisibility(View.VISIBLE);
                lunch.setVisibility(View.VISIBLE);
                lunchtextgrid.setVisibility(View.GONE);
                luncheditgrid.setVisibility(View.VISIBLE);
                lunchregularinsulintext.setVisibility(View.GONE);
                lunchNPHinsulintext.setVisibility(View.GONE);
                lunchUltrainsulintext.setVisibility(View.GONE);
                lunchregularinsulinedit.setVisibility(View.VISIBLE);
                lunchNPHinsulinedit.setVisibility(View.VISIBLE);
                lunchUltrainsulinedit.setVisibility(View.VISIBLE);
                lunchbloodfinish.setVisibility(View.VISIBLE);
                lunchsugaredit.setVisibility(View.VISIBLE);
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
                lunchtextgrid.setVisibility(View.VISIBLE);
                luncheditgrid.setVisibility(View.GONE);
                lunchbloodedit.setVisibility(View.VISIBLE);
                lunchsugartext.setVisibility(View.VISIBLE);
                lunchsugaredit.setVisibility(View.GONE);
                lunchbloodfinish.setVisibility(View.GONE);
                lunchregularinsulintext.setVisibility(View.VISIBLE);
                lunchNPHinsulintext.setVisibility(View.VISIBLE);
                lunchUltrainsulintext.setVisibility(View.VISIBLE);
                lunchregularinsulinedit.setVisibility(View.GONE);
                lunchNPHinsulinedit.setVisibility(View.GONE);
                lunchUltrainsulinedit.setVisibility(View.GONE);

                String lunchsugar = (String)lunchsugaredit.getText().toString(); // 입력한 점심 혈당을 lunchsugaredit에 입력
                lunchsugartext.setText(lunchsugar);

                String lregular = (String)lunchregularinsulinedit.getText().toString();
                lunchregularinsulintext.setText(lregular); // 입력한 아침 인슐린 입력

                String lNPH = (String)lunchNPHinsulinedit.getText().toString();
                lunchNPHinsulintext.setText(lNPH); // 입력한 아침 혈압을 lunchpressuretext에 입력

                String lUltra = (String)lunchUltrainsulinedit.getText().toString();
                lunchUltrainsulintext.setText(lUltra); // 입력한 아침 혈압을 lunchpressuretext에 입력

                time = "점심";
                if(lunchsugar.equals("")|lregular.equals("")|lNPH.equals("")|lUltra.equals("")){
                    notifyshow();
                }
                else{
                    mBloodSugar.create(Double.parseDouble(lunchsugar), Double.parseDouble(lregular),Double.parseDouble(lNPH),Double.parseDouble(lUltra) ,timeminutedate, time);
                }
            }

        });

        dinnerbloodedit.setOnClickListener(new View.OnClickListener() { // 저녁 행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Dinner.setVisibility(View.GONE);
                dinnerbloodedit.setVisibility(View.GONE);
                dinnersugartext.setVisibility(View.GONE);
                dinnerbloodfinish.setVisibility(View.VISIBLE);
                dinnertextgrid.setVisibility(View.GONE);
                dinnereditgrid.setVisibility(View.VISIBLE);
                dinner.setVisibility(View.VISIBLE);
                dinnerregularinsulintext.setVisibility(View.GONE);
                dinnerNPHinsulintext.setVisibility(View.GONE);
                dinnerUltrainsulintext.setVisibility(View.GONE);
                dinnerregularinsulinedit.setVisibility(View.VISIBLE);
                dinnerNPHinsulinedit.setVisibility(View.VISIBLE);
                dinnerUltrainsulinedit.setVisibility(View.VISIBLE);
                dinnersugaredit.setVisibility(View.VISIBLE);
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

            dinnertextgrid.setVisibility(View.VISIBLE);
            dinnereditgrid.setVisibility(View.GONE);

            dinnerbloodfinish.setVisibility(View.GONE);
            dinnersugaredit.setVisibility(View.GONE);
            dinnerregularinsulintext.setVisibility(View.VISIBLE);
            dinnerNPHinsulintext.setVisibility(View.VISIBLE);
            dinnerUltrainsulintext.setVisibility(View.VISIBLE);
            dinnerregularinsulinedit.setVisibility(View.GONE);
            dinnerNPHinsulinedit.setVisibility(View.GONE);
            dinnerUltrainsulinedit.setVisibility(View.GONE);

                String dinnersugar = (String)dinnersugaredit.getText().toString(); // 입력한 저녁 혈당을 lunchsugaredit에 입력
                dinnersugartext.setText(dinnersugar);


                String dregular = (String)dinnerregularinsulinedit.getText().toString();
                dinnerregularinsulintext.setText(dregular); // 입력한 아침 인슐린 입력

                String dNPH = (String)dinnerNPHinsulinedit.getText().toString();
                dinnerNPHinsulintext.setText(dNPH); // 입력한 아침 혈압을 dinnerpressuretext에 입력

                String dUltra = (String)dinnerUltrainsulinedit.getText().toString();
                dinnerUltrainsulintext.setText(dUltra); // 입력한 아침 혈압을 dinnerpressuretext에 입력



                time = "저녁";
                if(dinnersugar.equals("")|dregular.equals("")|dNPH.equals("")|dUltra.equals("")){
                    notifyshow();
                }
                else{
                    mBloodSugar.create(Double.parseDouble(dinnersugar), Double.parseDouble(dregular),Double.parseDouble(dNPH),Double.parseDouble(dUltra) ,timeminutedate, time);
                }
                Log.d(this.getClass().getName(),dinnersugar+"저녁혈당이거닷~!!!!!!1");

                //   mBloodSugar.create("userid", Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure), new Date(), time);

                //mBloodSugar.create(Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure),Double.parseDouble(dinnerpressure2), date, time);
                mBloodSugar.create(Double.parseDouble(dinnersugar), Double.parseDouble(dregular),Double.parseDouble(dNPH),Double.parseDouble(dUltra) ,timeminutedate, time);
             //   mBloodSugar.create("userid", Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure), new Date(), time);

            }

        });

        sleepbloodedit.setOnClickListener(new View.OnClickListener() { // 취침 후  행의 연필 아이콘 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                sleeptextgrid.setVisibility(View.GONE);
                sleepeditgrid.setVisibility(View.VISIBLE);
                sleepbloodfinish.setVisibility(View.VISIBLE);
                sleepsugaredit.setVisibility(View.VISIBLE);
                sleep.setVisibility(View.VISIBLE);

                sleepregularinsulintext.setVisibility(View.GONE);
                sleepNPHinsulintext.setVisibility(View.GONE);
                sleepUltrainsulintext.setVisibility(View.GONE);

                sleepregularinsulinedit.setVisibility(View.VISIBLE);
                sleepNPHinsulinedit.setVisibility(View.VISIBLE);
                sleepUltrainsulinedit.setVisibility(View.VISIBLE);


                Sleep.setVisibility(View.GONE);
                sleepbloodedit.setVisibility(View.GONE);
                sleepsugartext.setVisibility(View.GONE);

                sleepbloodfinish.setVisibility(View.VISIBLE);
                sleepsugaredit.setVisibility(View.VISIBLE);

                sleep.setVisibility(View.VISIBLE);

            }
        });

        sleepbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분
            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정
            //데베 삽입 X
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"체크 클릭");
                sleeptextgrid.setVisibility(View.VISIBLE);
                sleepeditgrid.setVisibility(View.GONE);
                sleep.setVisibility(View.GONE);
                Sleep.setVisibility(View.VISIBLE);
                sleepbloodedit.setVisibility(View.VISIBLE);
                sleepsugartext.setVisibility(View.VISIBLE);


                sleepbloodfinish.setVisibility(View.GONE);
                sleepsugaredit.setVisibility(View.GONE);
                sleepregularinsulintext.setVisibility(View.VISIBLE);
                sleepNPHinsulintext.setVisibility(View.VISIBLE);
                sleepUltrainsulintext.setVisibility(View.VISIBLE);
                sleepregularinsulinedit.setVisibility(View.GONE);
                sleepNPHinsulinedit.setVisibility(View.GONE);
                sleepUltrainsulinedit.setVisibility(View.GONE);

                sleepsugaredit.setVisibility(View.GONE);





                sleepbloodfinish.setVisibility(View.GONE);
                sleepsugaredit.setVisibility(View.GONE);


                String sleepsugar = (String)sleepsugaredit.getText().toString(); // 입력한 취침 후  혈당을 dinnersugaredit에 입력
        sleepsugartext.setText(sleepsugar);

                sleepsugartext.setText(sleepsugar);


                String sregular = (String)sleepregularinsulinedit.getText().toString();
                sleepregularinsulintext.setText(sregular); // 입력한 아침 인슐린 입력

                String sNPH = (String)sleepNPHinsulinedit.getText().toString();
                sleepNPHinsulintext.setText(sNPH); // 입력한 아침 혈압을 sleeppressuretext에 입력

                String sUltra = (String)sleepUltrainsulinedit.getText().toString();
                sleepUltrainsulintext.setText(sUltra); // 입력한 아침 혈압을 sleeppressuretext에 입력

                time = "취침전";
                Log.d(this.getClass().getName(),sleepsugar+"취침혈당이거닷~!!!!!!1");
                if(sleepsugar.equals("")|sregular.equals("")|sNPH.equals("")|sUltra.equals("")){
                    notifyshow();
                }
                else{
                    mBloodSugar.create(Double.parseDouble(sleepsugar), Double.parseDouble(sregular),Double.parseDouble(sNPH),Double.parseDouble(sUltra) ,timeminutedate, time);
                }
                //  mBloodSugar.create("userid", Double.parseDouble(sleepsugar), Double.parseDouble(sleeppressure), new Date(), time);

               // mBloodSugar.create(Double.parseDouble(sleepsugar), Double.parseDouble(sleeppressure),Double.parseDouble(sleeppressure2), date, time);
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
        public void signOut() {
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