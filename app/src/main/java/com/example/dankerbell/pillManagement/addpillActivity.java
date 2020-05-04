package com.example.dankerbell.pillManagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class addpillActivity extends AppCompatActivity { // 약 직접등록 클래스
    TextView back;
    private static final String DEFAULT_COLLECTION="NULL";
    private EditText mpill_name;
    private EditText mamount;
    private Spinner munit_amount;
    private EditText mcount;
    private CheckBox mw,mm,ml,me,mn;
    private Spinner mpilltime;
    private EditText mtimes;
    private Button mstore;

    private FirebaseFirestore mFirestore;

    // 디비 연결, 사용자 받아오는 거는 다 하나에서 하는 게 좋을듯

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpill);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() { // 뒤로 가기 클릭시 실행
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //각각의 입력 id에 맞는 입력값을 받아오는 부분
        //start get input
        mpill_name = findViewById(R.id.pill_name);
        mamount = findViewById(R.id.amount);

        munit_amount = (Spinner)findViewById(R.id.diabeteskindspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.my_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        munit_amount.setAdapter(adapter);

        mcount = findViewById(R.id.count);
        mw = (CheckBox)findViewById(R.id.wakeUp);
        mm = (CheckBox)findViewById(R.id.morning);
        ml = (CheckBox)findViewById(R.id.lunch);
        me = (CheckBox)findViewById(R.id.evening);
        mn = (CheckBox)findViewById(R.id.night);

        mpilltime = (Spinner)findViewById(R.id.pilltime);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.interval
                , android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mpilltime.setAdapter(adapter1);

        mtimes = findViewById(R.id.times);
        mstore = findViewById(R.id.store);
        //end

        addPill();
    }

    public void addPill(){
        //xml에서 받은 정보를 String형으로 다 변환
        //start convert
        final String pillName = mpill_name.getText().toString();
        final String amt = mamount.getText().toString();
        final String uamnt =munit_amount.getSelectedItem().toString();
        final String cnt = mcount.getText().toString();
        final String pilltime = mpilltime.getSelectedItem().toString();
        final String times =mtimes.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String User = user.getEmail();

        mstore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String takingTime="";
                if (mw.isChecked() == true)
                    takingTime += mw.getText().toString();
                if (mm.isChecked() == true)
                    takingTime += ","+mw.getText().toString();
                if (ml.isChecked() == true)
                    takingTime += ","+ mw.getText().toString();
                if (me.isChecked() == true)
                    takingTime+= ","+mw.getText().toString();
                if (mn.isChecked() == true)
                    takingTime+= mw.getText().toString();
                //end

                //디비에 올리는 부분, document가 중복이면 덮어쓰기가 되는 문제점이 아직 있음
                InfoaddPill addpill = new InfoaddPill(pillName, amt, uamnt,cnt, takingTime, pilltime,times);
                mFirestore=FirebaseFirestore.getInstance();

                mFirestore.collection("takingPill").document(User).set(addpill);
            }
        });
    }
}

