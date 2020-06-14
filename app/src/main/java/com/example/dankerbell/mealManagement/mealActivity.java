package com.example.dankerbell.mealManagement;

import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.Firebase.FoodlistCrud;
import com.example.dankerbell.Firebase.profileCrud;
import com.example.dankerbell.ProfileActivity;
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
    profileCrud mprofile=profileCrud.getInstance();
    RecyclerView mRecyclerView2;
    RecyclerView Recyclerlunch;
    RecyclerView Recyclerdinner;
    TextView toolbar;
    TextView close;
    DrawerLayout drawerLayout;
    View drawerView;
    Button logout;
    Button mypage;
    TextView todaycalory,mycalory;
    int recommendcal=0;
    double height=0.0,weight=0.0;
    int mkcal=0;
    int lkcal=0;
    int dkcal=0;
    String activity;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        searchmeal=new SearchmealActivity();
        mprofile.read();

        mRecyclerView2 = findViewById(R.id.recycler2); // 아침 recycler
        Recyclerlunch=findViewById(R.id.recycler_lunch);
        Recyclerdinner=findViewById(R.id.recycler_dinner);
        todaycalory=findViewById(R.id.todaycalory); //섭취 칼로링
        mycalory=findViewById(R.id.mycalory); //권장칼로링
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        Recyclerlunch.setLayoutManager(new LinearLayoutManager(this));
        Recyclerdinner.setLayoutManager(new LinearLayoutManager(this));
        com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter mAdapter1 = null ;
        com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter2 mAdapter2 = null ;
        com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter3 mAdapter3 = null ;
        toolbar=findViewById(R.id.toolbar_menu);
        drawerLayout=findViewById(R.id.drawer_layout) ;
        mypage = findViewById(R.id.mypage);
        logout = findViewById(R.id.logout);
        drawerView=findViewById(R.id.drawer);
        close=findViewById(R.id.toolbar_close);

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
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
        logout.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭

            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"로그아웃 클릭");
                //signOut();
            }
        });

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
                mymorningList.clear();
                mylunchList.clear();
                mydinnerList.clear();
                foodlistCrud.readmymorningmeal(yesterday);
                foodlistCrud.readmylunchmeal(yesterday);
                foodlistCrud.readmydinnermeal(yesterday);
                FoodlistCrud.mealHandler = new Handler(){
                    @Override public void handleMessage(Message msg){
                        if (msg.what==1001){
                            mylist();
//                            new RecyclerDBAdapter().getmo
//                            Log.d("오늘총칼로리",String.valueOf(foodlistCrud.totalkcal()));
//                            todaycalory.setText(foodlistCrud.totalkcal());
                        }
                    }};
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 > 버튼 클릭
                SimpleDateFormat sdf3 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, +1);  // 오늘 날짜에서 하루를 더함
                final String tomorrow = sdf3.format(calendar.getTime());
                mymorningList.clear();
                mylunchList.clear();
                mydinnerList.clear();
                foodlistCrud.readmymorningmeal(tomorrow);
                foodlistCrud.readmylunchmeal(tomorrow);
                foodlistCrud.readmydinnermeal(tomorrow);
                FoodlistCrud.mealHandler = new Handler(){
                    @Override public void handleMessage(Message msg){
                        if (msg.what==1001){
                            mylist();
//                            Log.d("오늘총칼로리",String.valueOf(foodlistCrud.totalkcal()));
//                            todaycalory.setText(foodlistCrud.totalkcal());
                        }
                    }};
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
        mymorningList.clear();
        mylunchList.clear();
        mydinnerList.clear();
        foodlistCrud.readmymorningmeal(date);
        foodlistCrud.readmylunchmeal(date);
        foodlistCrud.readmydinnermeal(date);
        FoodlistCrud.mealHandler = new Handler(){
            @Override public void handleMessage(Message msg){
                if (msg.what==1001){ // 파이어베이스 데이터를 받아오는 메소드에서 메세지 받음
                    Log.d("식단 읽기 메세지 받음","식단메시지!!!!");
                    if(!foodlistCrud.mymorningfood.isEmpty()){ // 파이어베이스에 저장된 아침식단 목록이 있다면 실행
                        op="아침db";
                        if(mymorningList.isEmpty()){
                            Log.d("식단 읽기 메세지 받음","아침식단 보이게 !!");
                            if(mymorningList.isEmpty()){
                                for(int i=0;i<foodlistCrud.mymorningfood.size();i++){
                                    addFoodKcalItem(foodlistCrud.mymorningfood.get(i),foodlistCrud.mymorningkcal.get(i),false);
                                }
                        }

                            com.example.dankerbell.mealManagement.RecyclerDBAdapter mymorningAdapter = new com.example.dankerbell.mealManagement.RecyclerDBAdapter(mealActivity.this,mymorningList);
                            mkcal=mymorningAdapter.getMorningkcal();
                            Log.d("아침칼로리",String.valueOf(mkcal));

                            mRecyclerView2.setAdapter(mymorningAdapter); // 어댑터
                    }}
                    if(!foodlistCrud.mylunchfood.isEmpty()){
                        op="점심db";
                        if(mylunchList.isEmpty()){
                        for(int i=0;i<foodlistCrud.mylunchfood.size();i++){
                            addFoodKcalItem(foodlistCrud.mylunchfood.get(i),foodlistCrud.mylunchkcal.get(i),false);
                        }
                        com.example.dankerbell.mealManagement.RecyclerDBAdapter2 mylunchAdapter = new com.example.dankerbell.mealManagement.RecyclerDBAdapter2(mealActivity.this,mylunchList);
                        lkcal=mylunchAdapter.getLunchkcal();
                            Log.d("점심칼로리",String.valueOf(lkcal));

                            Recyclerlunch.setAdapter(mylunchAdapter);
                    }}
                    if(foodlistCrud.mydinnerfood.isEmpty()!=true){  // attaching이 왜 안되죵 ?
                        op="저녁db";
                        if(mydinnerList.isEmpty()){
                        for(int i=0;i<foodlistCrud.mydinnerfood.size();i++){
                            addFoodKcalItem(foodlistCrud.mydinnerfood.get(i),foodlistCrud.mydinnerkcal.get(i),false);}
                        com.example.dankerbell.mealManagement.RecyclerDBAdapter3 mydinnerAdapter = new com.example.dankerbell.mealManagement.RecyclerDBAdapter3(mealActivity.this,mydinnerList);
                        dkcal=mydinnerAdapter.getDinnerkcal();
                        Recyclerdinner.setAdapter(mydinnerAdapter);
                    }}
                    if(getfood.isEmpty()){
                        for(int i=0;i<FoodlistCrud.getKcal().size();i++){
                            Log.d("음식", String.valueOf(i)+FoodlistCrud.getFood());
                            getfood.add(FoodlistCrud.getFood().get(i)); // db에있는 음식
                            getkcal.add(FoodlistCrud.getKcal().get(i)); // db에있는 칼로리
                            Log.d("칼로리", String.valueOf(i)+FoodlistCrud.getKcal());
                        }
                    }
                    int totalkcal=mkcal+lkcal+dkcal;
                    todaycalory.setText(String.valueOf(totalkcal));
                    morningsearch1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(this.getClass().getName(), "검색화면이동");
                            op="아침";
                            Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
                            startActivity(searchintent);// 음식검색화면으로 이동
                        }
                    });
                    searchlunch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(this.getClass().getName(), "검색화면이동");
                            op="점심";
                            Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
                            startActivity(searchintent);// 음식검색화면으로 이동
                        }
                    });
                    searchdinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(this.getClass().getName(), "검색화면이동");
                            op="저녁";
                            Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
                            startActivity(searchintent);// 음식검색화면으로 이동
                        }
                    });

                }
                else if(msg.what==1002){

                }
            }
        };

            if(op.equals("아침")){
                mAdapter1 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter(mealActivity.this,mymorningList);
                mRecyclerView2.setAdapter(mAdapter1);
            }
            else if(op.equals("점심")){
                mAdapter2 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter2(mealActivity.this,mylunchList);
                Recyclerlunch.setAdapter(mAdapter2);
            }
            else if(op.equals("저녁")){
               mAdapter3 = new com.example.dankerbell.mealManagement.RecyclerFoodKcalAdapter3(mealActivity.this,mydinnerList);
                Recyclerdinner.setAdapter(mAdapter3);
            }

        if(searchmeal.mfoodarraylist.isEmpty()&&op.equals("아침")){
            Log.d(this.getClass().getName(),"선택한아침배열없음");
        }
        else if(searchmeal.lfoodarraylist.isEmpty() && op.equals("점심")){
            Log.d(this.getClass().getName(),"선택한점심배열없음");

        }
        else if(searchmeal.dfoodarraylist.isEmpty() && op.equals("저녁")){
            Log.d(this.getClass().getName(),"선택한저녁배열없음");
        }
        else if(op.equals("아침") && searchmeal.mfoodarraylist.isEmpty()!=true){
            for(int i=0;i<searchmeal.mfoodarraylist.size();i++){
                String food=searchmeal.mfoodarraylist.get(i);
                String kcal=searchmeal.mkcalarraylist.get(i);
                addFoodKcalItem(food,kcal,false);
                Log.d("선택한 아침 음식"+i, food);
            }
        }
        else if(op.equals("점심") && searchmeal.lfoodarraylist.isEmpty()!=true){
            for(int i=0;i<searchmeal.lfoodarraylist.size();i++){
                String food=searchmeal.lfoodarraylist.get(i);
                String kcal=searchmeal.lkcalarraylist.get(i);
                addFoodKcalItem(food,kcal,false);
                Log.d("선택한 점심 음식"+i, food);
            }
        }
        else if(op.equals("저녁") && searchmeal.dfoodarraylist.isEmpty()!=true){
            for(int i=0;i<searchmeal.dfoodarraylist.size();i++){
                String food=searchmeal.dfoodarraylist.get(i);
                String kcal=searchmeal.dkcalarraylist.get(i);
                addFoodKcalItem(food,kcal,false);
                Log.d("선택한 저녁 음식"+i, food);
            }
        }
        if(mprofile.getMybmi().equals("")){
            mycalory.setText(String.valueOf(recommendcal)); //0
//0
        }
        else {
            height=Double.parseDouble(mprofile.getMyheight())/100;
            activity=mprofile.getMyactivity();
            Log.d("키 m단위",String.valueOf(height));

            if(mprofile.getMygender()=="여자"){
                   recommendcal=(int)(height*height*21); //권장체중

        }
            else{
                recommendcal=(int)(height*height*22); //권장체중

            }

            if(activity.equals("적음"))
                recommendcal=recommendcal*25;
            else if(activity.equals("보통"))
                recommendcal=recommendcal*35;
            else if(activity.equals("많음"))
                recommendcal=recommendcal*40;

            Log.d("권장칼로리",String.valueOf(recommendcal));

            mycalory.setText(String.valueOf(recommendcal));

        }

        morningfinish.setOnClickListener(new View.OnClickListener() { // 아침식단 체크버튼 클릭 시 실행
                @Override
            public void onClick(View view) {

                Log.d(this.getClass().getName(),"아침 체크버튼 클릭");
                for(int i=0;i<mymorningList.size();i++){
                    foodlistCrud.create("아침",date,mymorningList.get(i).getMyfood(),mymorningList.get(i).getMykcal());
                }
                    Log.d("아침배열", String.valueOf(mymorningList.size()));
                    Log.d("런치배열", String.valueOf(mylunchList.size()));
                }
        });

        final TextView lunchmealmore=findViewById(R.id.lunchmealmore); // 점식식단 펼치기
        final TextView lunchmealless=findViewById(R.id.lunchmealless); // 점심식단 접기
        final LinearLayout lunchmeal=findViewById(R.id.lunchmeal); // 점심식단 펼치기 클릭하면 나타나는 화면
        final TextView lunchfinish=findViewById(R.id.lunchfinish); // 점심식단 체크버튼
        final TextView dinnermealmore=findViewById(R.id.dinnermealmore);
        final TextView dinnermealless=findViewById(R.id.dinnermealless);
        final LinearLayout dinnermeal=findViewById(R.id.dinnermeal);
        morningmeal.setVisibility(View.VISIBLE); // 아침식단 작성 layout 생성
        lunchmeal.setVisibility(View.VISIBLE); //점심식단 작성 layout 사라짐
        dinnermeal.setVisibility(View.VISIBLE);
        morningfinish.setVisibility(View.VISIBLE);
        lunchfinish.setVisibility(View.VISIBLE);
        final TextView dinnerfinish=findViewById(R.id.dinnerfinish);
        dinnerfinish.setVisibility(View.VISIBLE);

        morningmealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 아침 펼치기 클릭 시 실행
                Log.d(this.getClass().getName(),"펼치기 클릭");
                morningmealmore.setVisibility(View.GONE); // 펼치기 버튼 사라짐
                morningfinish.setVisibility(View.VISIBLE);
           //     morningmealless.setVisibility(View.VISIBLE); // 접기 버튼 생성
                morningmealless2.setVisibility(View.VISIBLE); // 접기 버튼 생성
              //  morningmeal.setVisibility(View.VISIBLE); // 아침식단 작성 layout 생성
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
        lunchfinish.setOnClickListener(new View.OnClickListener() { // 점심식단 체크버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"점심 체크버튼 클릭");
                for(int i=0;i<mylunchList.size();i++){
                    foodlistCrud.create("점심",date,mylunchList.get(i).getMyfood(),mylunchList.get(i).getMykcal());
                }
                mymorningList.clear();
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
        dinnerfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"저녁 체크버튼 클릭");
                for(int i=0;i<mydinnerList.size();i++){
                    foodlistCrud.create("저녁",date,mydinnerList.get(i).getMyfood(),mydinnerList.get(i).getMykcal());
                }
                //mydinnerList.clear();
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

        if(op.equals("아침")){
            Log.d(this.getClass().getName(),"아침이당");
            mymorningList.add(item);}
        else if(op.equals("점심")){
            Log.d(this.getClass().getName(),"점심이당");
            mylunchList.add(item);}
        else if(op.equals("저녁")){
            Log.d(this.getClass().getName(),"저녁이당");
            mydinnerList.add(item);
        }
        else if(op.equals("아침db")){
            Log.d(this.getClass().getName(),"아침디비당 너만되면어떡하니?ㅡㅡ");
                if(mymorningList.size()<FoodlistCrud.mymorningfood.size()){
                    Log.d("mymorningList 사이즈", String.valueOf(mymorningList.size()));
                    Log.d("디비 배열 사이즈", String.valueOf(FoodlistCrud.mymorningfood.size()));
                    mymorningList.add(item);
                }
            }
        else if(op.equals("점심db")){
            Log.d(this.getClass().getName(),"점심디비당");
             if(mylunchList.size()<FoodlistCrud.mylunchfood.size()){
                 Log.d("mylunchList 사이즈", String.valueOf(mylunchList.size()));
                 Log.d("디비 배열 사이즈", String.valueOf(FoodlistCrud.mylunchfood.size()));
                mylunchList.add(item);
            }
        }
        else if(op.equals("저녁db")){
            Log.d(this.getClass().getName(),"저녁디비당");
                if(mydinnerList.size()<FoodlistCrud.mydinnerfood.size()){
                    Log.d("mydinnerList 사이즈", String.valueOf(mydinnerList.size()));
                     Log.d("디비 배열 사이즈", String.valueOf(FoodlistCrud.mydinnerfood.size()));
                    mydinnerList.add(item);
            }
        }
        }
    void mylist(){
        Log.d(this.getClass().getName(),"mylist 실행");

        if(foodlistCrud.mymorningfood.isEmpty()!=true){ // 파이어베이스에 저장된 아침식단 목록이 있다면 실행
            op="아침db";
            if(mymorningList.isEmpty()){
                for(int i=0;i<foodlistCrud.mymorningfood.size();i++){
                    addFoodKcalItem(foodlistCrud.mymorningfood.get(i),foodlistCrud.mymorningkcal.get(i),false);
                }
                com.example.dankerbell.mealManagement.RecyclerDBAdapter mymorningAdapter = new com.example.dankerbell.mealManagement.RecyclerDBAdapter(mealActivity.this,mymorningList);
                mkcal=mymorningAdapter.getMorningkcal();

                mRecyclerView2.setAdapter(mymorningAdapter); // 어댑터
            }}
        if(!foodlistCrud.mylunchfood.isEmpty()){
            op="점심db";
            if(mylunchList.isEmpty()){
                for(int i=0;i<foodlistCrud.mylunchfood.size();i++){
                    addFoodKcalItem(foodlistCrud.mylunchfood.get(i),foodlistCrud.mylunchkcal.get(i),false);
                }
                com.example.dankerbell.mealManagement.RecyclerDBAdapter2 mylunchAdapter = new com.example.dankerbell.mealManagement.RecyclerDBAdapter2(mealActivity.this,mylunchList);
                lkcal=mylunchAdapter.getLunchkcal();

                Recyclerlunch.setAdapter(mylunchAdapter);
            }}
        if(foodlistCrud.mydinnerfood.isEmpty()!=true){  // attaching이 왜 안되죵 ?
            op="저녁db";
            if(mydinnerList.isEmpty()){
                for(int i=0;i<foodlistCrud.mydinnerfood.size();i++){
                    addFoodKcalItem(foodlistCrud.mydinnerfood.get(i),foodlistCrud.mydinnerkcal.get(i),false);}
                com.example.dankerbell.mealManagement.RecyclerDBAdapter3 mydinnerAdapter = new com.example.dankerbell.mealManagement.RecyclerDBAdapter3(mealActivity.this,mydinnerList);
                dkcal=mydinnerAdapter.getDinnerkcal();
                Recyclerdinner.setAdapter(mydinnerAdapter);
            }}
            int totalkcal=mkcal+lkcal+dkcal;
            todaycalory.setText(String.valueOf(totalkcal));
    }

    }


