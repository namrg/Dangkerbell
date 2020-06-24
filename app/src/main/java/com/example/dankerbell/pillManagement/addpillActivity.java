package com.example.dankerbell.pillManagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.dankerbell.Firebase.CrudInterface.db;


public class addpillActivity extends AppCompatActivity {
    pillCrud mPill = pillCrud.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    TextView back;
    private EditText mPillname; //약이름
    private EditText mamount; //복용량
    private Spinner mUnit_amount; //복용량 단위
    private EditText mcount; //복용 횟수
    private CheckBox mWakeup, mMoring, mLunch, mEvening, mNight; //takingPillTime => 복용시간
    private Spinner mpillTime; //식전 식후
    private EditText mTimes; //몇분후
    private Button mstore;
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_addpill);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() { // 뒤로 가기 클릭시 실행
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //각각의 입력 id에 맞는 입력값을 받아오는 부분
        mPillname = findViewById(R.id.pill_name);
        mamount = findViewById(R.id.amount);
        mcount = findViewById(R.id.count);
        mTimes = findViewById(R.id.times);
        mstore = findViewById(R.id.store);

        mWakeup = (CheckBox)findViewById(R.id.wakeUp);
        mMoring = (CheckBox)findViewById(R.id.morning);
        mLunch = (CheckBox)findViewById(R.id.lunch);
        mEvening = (CheckBox)findViewById(R.id.evening);
        mNight = (CheckBox)findViewById(R.id.night);

        mUnit_amount = (Spinner)findViewById(R.id.diabeteskindspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.my_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mUnit_amount.setAdapter(adapter);

        mpillTime =(Spinner)findViewById(R.id.pilltime);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.interval, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mpillTime.setAdapter(adapter1);

        mstore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pillname = mPillname.getText().toString();
                Log.d("약이름",pillname);
                String amountstring=mamount.getText().toString();
                String unit_amount = mUnit_amount.getSelectedItem().toString();
                Log.d("복용 단위",unit_amount);
                String countstring=mcount.getText().toString();
                String pilltime = mpillTime.getSelectedItem().toString();
                Log.d("복용시간",pilltime);
                String timestring=mTimes.getText().toString();
                String takingTime = " ";
                if (mWakeup.isChecked() == true)
                    takingTime += mWakeup.getText().toString();
                if (mMoring.isChecked() == true)
                    takingTime += "," + mMoring.getText().toString();
                if (mLunch.isChecked() == true)
                    takingTime += "," + mLunch.getText().toString();
                if (mEvening.isChecked() == true)
                    takingTime += "," + mEvening.getText().toString();
                if (mNight.isChecked() == true)
                    takingTime += mNight.getText().toString();
                if(pillname.length()==0|unit_amount.length()==0|pilltime.length()==0|takingTime.equals("")|takingTime.length()==0|amountstring.equals("")|amountstring.length()==0|countstring.equals("")|countstring.length()==0){
                    notifyshow();
                }
                else if(!isStringDouble(amountstring)|!isStringDouble(timestring)|!isStringDouble(countstring)){
                    stringerror();
                }
                else{
                    int amount=Integer.parseInt(amountstring);
                    int count=Integer.parseInt(countstring);
                    int times = Integer.parseInt(timestring);

                    mPill.create(User, pillname, amount, unit_amount, count, takingTime, pilltime, times);
                    Intent pillintent = new Intent(getApplicationContext(), pillActivity.class);
                    startActivity(pillintent);}
            }
        });
    }
    public static boolean isStringDouble(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    void stringerror(){
        Log.d(this.getClass().getName(),"정보 다 입력하고 저장하기 클릭");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("숫자로만 입력해주세요.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
            }
        });
        builder.show();
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
}
