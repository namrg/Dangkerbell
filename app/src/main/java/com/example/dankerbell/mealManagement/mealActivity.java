package com.example.dankerbell.mealManagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.Firebase.FoodlistCrud;
import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.pillManagement.pillActivity;
//import com.example.dankerbell.pillManagement.pillActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class mealActivity extends AppCompatActivity { // 식단관리 클래스
    TextView home;

    static ArrayList<RecyclermyfoodItem> mymorningList = new ArrayList<RecyclermyfoodItem>();
    static ArrayList<RecyclermyfoodItem> mylunchList = new ArrayList<RecyclermyfoodItem>();
    static ArrayList<RecyclermyfoodItem> mydinnerList = new ArrayList<RecyclermyfoodItem>();
    static String op="0";
    static final ArrayList<String> getfood=new ArrayList<>();
    static final ArrayList<String> getkcal=new ArrayList<>();
    TextView morningfood1;
    static Handler mHandler2=new Handler();
    TextView blood_txt,pill_txt;
    SearchmealActivity searchmeal;
    FoodlistCrud foodlistCrud=FoodlistCrud.getInstance();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        searchmeal=new SearchmealActivity();

       final RecyclerView mRecyclerView2 = findViewById(R.id.recycler2); // 아침 recycler
        final RecyclerView Recyclerlunch=findViewById(R.id.recycler_lunch);
        final RecyclerView Recyclerdinner=findViewById(R.id.recycler_dinner);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        Recyclerlunch.setLayoutManager(new LinearLayoutManager(this));
        Recyclerdinner.setLayoutManager(new LinearLayoutManager(this));
        com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter mAdapter1 = null ;

        //날짜 설정
        final TextView currentdate=findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String date = sdf.format(calendar.getTime());
        currentdate.setText(date);
        TextView prev=findViewById(R.id.prev);
        TextView next=findViewById(R.id.next);
        prev.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) { // 상단에 날짜 중 < 버튼 클릭
                SimpleDateFormat sdf2 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌.
                String yesterday = sdf2.format(calendar.getTime());
                currentdate.setText(yesterday);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 > 버튼 클릭
                SimpleDateFormat sdf3 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, +1);  // 오늘 날짜에서 하루를 더함
                String tomorrow = sdf3.format(calendar.getTime());
                currentdate.setText(tomorrow);
            }
        });
        final TextView morningmealless2=findViewById(R.id.morningmealless2);
        final TextView morningmealmore=findViewById(R.id.morningmealmore); // 아침 식단 펼치기
        final TextView morningmealless=findViewById(R.id.morningmealless); // 아침 식단 접기
        final LinearLayout morningmeal=findViewById(R.id.morningmeal); // 아침 식단 총 layout
        final TextView morningfinish=findViewById(R.id.morningfinish); // 아침식단 체크 버튼
        final TextView morningsearch1=findViewById(R.id.searchmorning);
        final TextView searchlunch=findViewById(R.id.searchlunch); // 돋보기 아이콘
        final TextView searchdinner=findViewById(R.id.searchdinner); // 돋보기 아이콘
        FoodlistCrud.readfoodList();
        FoodlistCrud.mealHandler = new Handler(){
            @Override public void handleMessage(Message msg){
                if (msg.what==1001){
                    if(getfood.isEmpty()){
                        for(int i=0;i<FoodlistCrud.getKcal().size();i++){
                            Log.d("음식", String.valueOf(i)+FoodlistCrud.getFood());
                            getfood.add(FoodlistCrud.getFood().get(i)); // db에있는 음식
                            getkcal.add(FoodlistCrud.getKcal().get(i)); // db에있는 칼로리
                            Log.d("칼로리", String.valueOf(i)+FoodlistCrud.getKcal());
                        }
                    }
                    morningsearch1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(this.getClass().getName(), "검색화면이동");
                            op="아침";
//                            com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mymorningList);
//                            mRecyclerView2.setAdapter(mAdapter1);
                            Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
                            startActivity(searchintent);// 음식검색화면으로 이동

                        }
                    });
                    searchlunch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(this.getClass().getName(), "검색화면이동");
                            op="점심";
//                            com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mylunchList);
//                            Recyclerlunch.setAdapter(mAdapter1);
                            Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
                            startActivity(searchintent);// 음식검색화면으로 이동
                        }
                    });
                    searchdinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(this.getClass().getName(), "검색화면이동");
                            op="저녁";
//                            com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mydinnerList);
//                            Recyclerdinner.setAdapter(mAdapter1);
                            Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
                            startActivity(searchintent);// 음식검색화면으로 이동

                        }
                    });
                }
            }
        };

            if(op=="아침"){
                mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mymorningList);
                mRecyclerView2.setAdapter(mAdapter1);
            }
            else if(op=="점심"){
                mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mylunchList);
                Recyclerlunch.setAdapter(mAdapter1);
            }
            else{
               mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mydinnerList);
                Recyclerdinner.setAdapter(mAdapter1);
            }

//        searchlunch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(this.getClass().getName(), "검색화면이동");
//                final String time="점심";
//
//                com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mymorningList);
//                Recyclerlunch.setAdapter(mAdapter1);
//                Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
//                startActivity(searchintent);// 음식검색화면으로 이동
//            }
//        });
//        searchdinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(this.getClass().getName(), "검색화면이동");
//                final String time="저녁";
//                com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mydinnerList);
//                Recyclerdinner.setAdapter(mAdapter1);
//                Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
//                startActivity(searchintent);// 음식검색화면으로 이동
//
//            }
//        });
        if(searchmeal.getFoodarraylist().isEmpty()){
            Log.d(this.getClass().getName(),"배열없음");
        }
        else{
        for(int i=0;i<searchmeal.getFoodarraylist().size();i++){
            String food=searchmeal.getFoodarraylist().get(i);
            String kcal=searchmeal.getKcalarraylist().get(i);
            addFoodKcalItem(food,kcal,false);
            Log.d("받아온 음식1", food);
        }}




        morningfinish.setOnClickListener(new View.OnClickListener() { // 아침식단 체크버튼 클릭 시 실행
                @Override
            public void onClick(View view) {

                Log.d(this.getClass().getName(),"아침 체크버튼 클릭");
                for(int i=0;i<mymorningList.size();i++){
                    foodlistCrud.create("아침",date,mymorningList.get(i).getMyfood(),mymorningList.get(i).getMykcal());
                }
                mymorningList.clear();
                mylunchList.clear();
                mydinnerList.clear();

                }
        });



        morningmealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 아침 펼치기 클릭 시 실행
                Log.d(this.getClass().getName(),"펼치기 클릭");
                morningmealmore.setVisibility(View.GONE); // 펼치기 버튼 사라짐
                morningfinish.setVisibility(View.VISIBLE);
           //     morningmealless.setVisibility(View.VISIBLE); // 접기 버튼 생성
                morningmealless2.setVisibility(View.VISIBLE); // 접기 버튼 생성

                morningmeal.setVisibility(View.VISIBLE); // 아침식단 작성 layout 생성
            }
        });
        morningmealless2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 아침 접기 클릭 시 실행
                Log.d(this.getClass().getName(),"접기 클릭");
                morningmealmore.setVisibility(View.VISIBLE); //펼치기 버튼 생성
                morningfinish.setVisibility(View.GONE);
                morningmealless2.setVisibility(View.GONE); // 접기 버튼 생성

            //    morningmealless.setVisibility(View.GONE); // 접기 버튼 사라짐
                morningmeal.setVisibility(View.GONE); // 아침식단 작성 layout 사라짐
            }
        });

        final TextView lunchmealmore=findViewById(R.id.lunchmealmore); // 점식식단 펼치기
        final TextView lunchmealless=findViewById(R.id.lunchmealless); // 점심식단 접기
        final LinearLayout lunchmeal=findViewById(R.id.lunchmeal); // 점심식단 펼치기 클릭하면 나타나는 화면
//        final LinearLayout lunchtext1=findViewById(R.id.lunchtext1); // 점심식단입력창1
//        final LinearLayout lunchtext2=findViewById(R.id.lunchtext2); // 점심식단입력창2
//        final LinearLayout lunchtext3=findViewById(R.id.lunchtext3); // 점심식단입력창3
//        final ImageView addlunchtext1=findViewById(R.id.addlunchtext1); // 점심식단입력창1에 + 버튼1
//        final ImageView addlunchtext2=findViewById(R.id.addlunchtext2); // 점심식단입력창2에 + 버튼2
//        final ImageView addlunchtext3=findViewById(R.id.addlunchtext3); // 점심식단입력창3에 + 버튼3
        final TextView lunchfinish=findViewById(R.id.lunchfinish); // 점심식단 체크버튼

        lunchfinish.setOnClickListener(new View.OnClickListener() { // 점심식단 체크버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"점심 체크버튼 클릭");
                for(int i=0;i<mylunchList.size();i++){
                    foodlistCrud.create("점심",date,mylunchList.get(i).getMyfood(),mylunchList.get(i).getMykcal());
                }
                mymorningList.clear();
                mylunchList.clear();
                mydinnerList.clear();
                Log.d("아침배열", String.valueOf(mymorningList.size()));
                Log.d("런치배열", String.valueOf(mylunchList.size()));

            }
        });
        lunchmealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 점심 펼치기 클릭 시 실행
                Log.d(this.getClass().getName(),"펼치기 클릭");
                lunchmealmore.setVisibility(View.GONE); // 펼치기 버튼 사라짐
                lunchfinish.setVisibility(View.VISIBLE);
                lunchmealless.setVisibility(View.VISIBLE); // 접기 버튼 생성
                lunchmeal.setVisibility(View.VISIBLE); //점심식단 작성 layout 사라짐
            }
        });
        lunchmealless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 점심 접기 클릭 시 실행
                Log.d(this.getClass().getName(),"접기 클릭");
                lunchmealmore.setVisibility(View.VISIBLE); // 펼치기 버튼 생성
                lunchfinish.setVisibility(View.GONE);
                lunchmealless.setVisibility(View.GONE); // 접기 버튼 사라짐
                lunchmeal.setVisibility(View.GONE); // 점식식단 작성 layot 사라짐
            }
        });


        final TextView dinnermealmore=findViewById(R.id.dinnermealmore);
        final TextView dinnermealless=findViewById(R.id.dinnermealless);
        final LinearLayout dinnermeal=findViewById(R.id.dinnermeal);

        final TextView dinnerfinish=findViewById(R.id.dinnerfinish);

        dinnerfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"저녁 체크버튼 클릭");
                for(int i=0;i<mydinnerList.size();i++){
                    foodlistCrud.create("저녁",date,mydinnerList.get(i).getMyfood(),mydinnerList.get(i).getMykcal());
                }
                mymorningList.clear();
                mylunchList.clear();
                mydinnerList.clear();

            }
        });
        dinnermealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 저녁 펼치기 클릭 시 실행
                Log.d(this.getClass().getName(),"펼치기 클릭");
                dinnermealmore.setVisibility(View.GONE); // 펼치기 버튼 사라짐
                dinnerfinish.setVisibility(View.VISIBLE);
                dinnermealless.setVisibility(View.VISIBLE); // 접기 버튼 생성
                dinnermeal.setVisibility(View.VISIBLE); // 저녁식단 작성 layout 생성
            }
        });

        dinnermealless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 저녁 접기 클릭 시 실행
                Log.d(this.getClass().getName(),"접기 클릭");
                dinnermealmore.setVisibility(View.VISIBLE); // 펼치기 버튼 생성
                dinnerfinish.setVisibility(View.GONE);
                dinnermealless.setVisibility(View.GONE); // 접기 버튼 사라짐
                dinnermeal.setVisibility(View.GONE); // 저녁식단 작성 layout 사라짐
            }
        });

        home=findViewById(R.id.home_txt);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        blood_txt=findViewById(R.id.blood_txt); // 상단에 있는 혈당관리 TextView
        pill_txt=findViewById(R.id.pill_txt); // 상단에 있는 복약관리 TextView
        blood_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 혈당관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent bloodintent = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(bloodintent);// 혈당관리 화면으로 이동하도록 bloodActivity로 전환
            }
        });
        pill_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 복약관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent pillintent = new Intent(getApplicationContext(), pillActivity.class);
               startActivity(pillintent);// 복약관리 화면으로 이동하도록 pillActivity로 전환
            }
        });
    }

    void addFoodKcalItem(String food, String kcal,Boolean isDeleted) {
        RecyclermyfoodItem item=new RecyclermyfoodItem(food,kcal,false);
        item.setMyfood(food);
        item.setMykcal(kcal);
        item.setisDeleted(false);
        if(op=="아침"){
            Log.d(this.getClass().getName(),"아침이당");
            mymorningList.add(item);}
        else if(op=="점심"){
            Log.d(this.getClass().getName(),"점심이당");
            mylunchList.add(item);}
        else {
            Log.d(this.getClass().getName(),"저녁이당");
            mydinnerList.add(item);
        }

    }

}
