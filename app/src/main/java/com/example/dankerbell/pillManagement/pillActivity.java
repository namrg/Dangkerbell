package com.example.dankerbell.pillManagement;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.RecyclerItem;
import com.example.dankerbell.mealManagement.mealActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    ArrayList<RecyclerpillItem> mypillList=new ArrayList<RecyclerpillItem>();
    RecyclerpillAdapter recyclerpillAdapter;

  //final String pillName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expill);
        pillRecycler = findViewById(R.id.pillrecycler);
        pillRecycler.setLayoutManager(new LinearLayoutManager(this));
        mPill.read();
        blood_txt = findViewById(R.id.blood_txt); // 상단에 있는 혈당관리 TextView
        meal_txt = findViewById(R.id.meal_txt); // 상단에 있는 식단관리 TextView
        blood_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 혈당관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent bloodintent = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(bloodintent);// 혈당관리 화면으로 이동하도록 bloodActivity로 전환
            }
        });
        meal_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 식단관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent mealintent = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(mealintent);// 식단관리 화면으로 이동하도록 mealActivity로 전환
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
        item.setNotify(notify);
        mypillList.add(item);
    }
        //데이터 삭제
}

