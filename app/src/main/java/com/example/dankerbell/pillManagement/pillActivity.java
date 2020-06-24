package com.example.dankerbell.pillManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.LoginInActivity;
import com.example.dankerbell.ProfileActivity;
import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.RecyclerItem;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.myprofileActivity;
import com.example.dankerbell.mysetTimeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

import static com.example.dankerbell.Firebase.CrudInterface.db;

public class pillActivity extends AppCompatActivity { // 복약관리 클래스
    pillCrud mPill = pillCrud.getInstance();
    TextView home; // 당커벨 (어플이름 )
    TextView blood_txt, meal_txt, med_name,amount;
    Button register_btn;
    RecyclerView pillRecycler;
    Button drawer_pill,drawer_meal,drawer_blood;
    DrawerLayout drawerLayout;
    View drawerView;
    Button mypage;
    Button logout,settime;
    TextView toolbar;
    TextView close;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    ArrayList<RecyclerpillItem> mypillList=new ArrayList<RecyclerpillItem>();
    RecyclerpillAdapter recyclerpillAdapter;
    TextView userid;
  //final String pillName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expill);
        pillRecycler = findViewById(R.id.pillrecycler);
        pillRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPill.read();
        blood_txt = findViewById(R.id.blood_txt); // 상단에 있는 혈당관리 TextView
        meal_txt = findViewById(R.id.meal_txt); // 상단에 있는 식단관리 TextView
        settime=findViewById(R.id.settime);

        toolbar=findViewById(R.id.toolbar_menu);
        drawerLayout=findViewById(R.id.drawer_layout) ;
        mypage = findViewById(R.id.mypage);
        logout = findViewById(R.id.logout);
        drawerView=findViewById(R.id.drawer);
        close=findViewById(R.id.toolbar_close);
        userid=findViewById(R.id.userid); // !!!!!!!
        userid.setText(User);

        blood_txt=findViewById(R.id.blood_txt); // 상단에 있는 혈당관리 TextView
        meal_txt=findViewById(R.id.pill_txt); // 상단에 있는 복약관리 TextView
        blood_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 혈당관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent bloodintent = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(bloodintent);// 혈당관리 화면으로 이동하도록 bloodActivity로 전환
            }
        });
//        meal_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 식단관리 클릭 시 실행
//            @Override
//            public void onClick(View view) {
//                Intent mealintent = new Intent(getApplicationContext(), mealActivity.class);
//                startActivity(mealintent);// 복약관리 화면으로 이동하도록 mealActivity로 전환
//            }
//        });
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
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setTimeintent = new Intent(getApplicationContext(), mysetTimeActivity.class);
                startActivity(setTimeintent);//액티비티 띄우기
            }
        });
        blood_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bloodintent = new Intent(getApplicationContext(),bloodActivity.class);
                startActivity(bloodintent);//액티비티 띄우기
            }
        });
        meal_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mealintent = new Intent(getApplicationContext(),mealActivity.class);
                startActivity(mealintent);//액티비티 띄우기
            }
        });

        drawer_blood=findViewById(R.id.drawer_blood);
        drawer_meal=findViewById(R.id.drawer_meal);
        drawer_pill=findViewById(R.id.drawer_pill);
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


        register_btn = findViewById(R.id.register_btn); // 약 직접등록하기 버튼
        register_btn.setOnClickListener(new View.OnClickListener() { // 약 직접등록하기 버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent addpillintent = new Intent(getApplicationContext(), addpillActivity.class);
                startActivity(addpillintent);// 약 직접등록 화면으로 이동하도록 addpillActivity로 전환
            }
        });
        home = findViewById(R.id.home_txt);
        home.setOnClickListener(new View.OnClickListener() { // 당커벨 클릭 시 홈화면으로 전환
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), homeActivity.class);
                startActivity(intent);//
            }
        });
        mPill.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) { // 메세지 받고 실행
                if (msg.what == 1002) {
                    Log.d(this.getClass().getName(), "메세지 받음");

                    mypillList.clear();
                    if (mypillList.isEmpty()) {
                        Log.d(this.getClass().getName(), "mList비어져잇음");
                        for (int i = 0; i < mPill.pillNamelist.size(); i++) { //파이어베이스에서 받아온 코드
                            addItem(mPill.pillNamelist.get(i), mPill.amountlist.get(i), mPill.alarmlist.get(i));
                            Log.d(this.getClass().getName(), String.valueOf(mPill.pillNamelist.size()));
                        }
                    }
                    recyclerpillAdapter = new RecyclerpillAdapter(pillActivity.this, mypillList);
                    pillRecycler.setAdapter(recyclerpillAdapter);
                }
            }




        };
    }

    public void init(){
        for(int i=0;i<mypillList.size();i++){
            mypillList.clear();
        }}
    private void addItem(String medname, int amount, boolean notify) {
        RecyclerpillItem item=new RecyclerpillItem(medname,amount,notify,false);
        item.setMedname(medname);
        item.setPill_amount(amount);
        item.setNotify(false);
        mypillList.add(item);
    }
        //데이터 삭제
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

