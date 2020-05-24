package com.example.dankerbell;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.Firebase.profileCrud;

public class myprofileActivity extends AppCompatActivity {
    TextView mgender,mbirth,mbmi,mdiabeteskind,mheight,mweight,mdisease,mhypertension,msmoke,mdiabetesyear,mheal,mactivity,mweightchange;
    profileCrud mprofile = profileCrud.getInstance();
    Button modifybtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mprofile.read();
        setContentView(R.layout.activity_myprofile);
        mgender=findViewById(R.id.mgender);
        mbirth=findViewById(R.id.mbrith);
        mbmi=findViewById(R.id.mbmi);
        mdiabeteskind=findViewById(R.id.mdiabeteskind);
        mheight=findViewById(R.id.mheight);
        mweight=findViewById(R.id.mweight);
        mdisease=findViewById(R.id.mdisease);
        mhypertension=findViewById(R.id.mhypertension);
        msmoke=findViewById(R.id.msmoke);
        mdiabetesyear=findViewById(R.id.mdiabetesyear);
        mheal=findViewById(R.id.mheal);
        mactivity=findViewById(R.id.mactivity);
        mweightchange=findViewById(R.id.mweightchange);
        modifybtn=findViewById(R.id.modifybtn);
        String mygender=mprofile.getMygender();
        String mybmi=mprofile.getMybmi();

        modifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);//홈화면 전환
            }
        });
        mprofile.pHandler = new Handler(){
            @Override public void handleMessage(Message msg){
                if (msg.what==1007){
                    if(!mgender.equals("")) {
                        mgender.setText(mprofile.getMygender());
                        mbmi.setText(mprofile.getMybmi());
                        mbirth.setText(mprofile.getMybirthday());
                        mdiabeteskind.setText(mprofile.getMydiabeteskind()); // x
                        mheight.setText(mprofile.getMyheight());
                        mweight.setText(mprofile.getMyweight());
                        mdisease.setText(mprofile.getMyunderlyingdisease());
                        mhypertension.setText(mprofile.getMyhighbloodpressure()); //x
                        msmoke.setText(mprofile.getMysmoke());
                        mdiabetesyear.setText(mprofile.getMyyear());
                        mheal.setText(mprofile.getMyhealdiabetes());
                        mactivity.setText(mprofile.getMyactivity());
                        mweightchange.setText(mprofile.getMyweightchange());
                    }
                }
            }
        };




    }
}
