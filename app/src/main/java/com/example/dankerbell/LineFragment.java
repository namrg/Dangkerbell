package com.example.dankerbell;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;


import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.bloodManagement.cholesterolCrud;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import im.dacer.androidcharts.LineView;

import static com.example.dankerbell.homeActivity.blood;

public class LineFragment extends Fragment {
    static LineFragment instance;
    ArrayList<Float> realBG = new ArrayList<>();
    ArrayList<Float> predBG = new ArrayList<>();
    ArrayList<String> dateTime;
    LineView lineViewFloat;

    public static LineFragment getInstance() {
        if (instance == null) {
            instance = new LineFragment();
        }
        return instance;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        ArrayList<String> tmp = new ArrayList<>();
        Log.i("create","view");
        View rootView = inflater.inflate(R.layout.fragment_line, container, false);
        lineViewFloat = (LineView) rootView.findViewById(R.id.line_view_float);
        if(lineViewFloat ==null){ Log.i("뭐헤","Tlqkf");}
        initLineView(lineViewFloat);
        return rootView;
    }
    //데이터 로드
    //로드 후 1주일치 이상이 아니면 예측 보여주지 않음
    //데이터가 1주일 이상이다. ForecastingBG

    private void initLineView(LineView lineView) {
        //label
        dateTime = new ArrayList<String>();
        dateTime.add("기상 후");
        dateTime.add("아침");
        dateTime.add("점심");
        dateTime.add("저녁");
        dateTime.add("취침 전");
        Log.i("허허","사벌");
        homeActivity h=new homeActivity();
        ArrayList<String> dlist = h.blood;
        Log.i("허허", String.valueOf(blood.isEmpty()));

        for(String d : dlist){
            Log.i("dlist @@@ ",d);
            if(d.length() == 0){
                Log.i("!!!", String.valueOf(d.length()));
            }
        }

        for(int i = 0; i<4; i++){
            Log.i("???", dlist.get(i));

            if (i == 0 && dlist.get(i).length() == 0){
                realBG.add((float) 0.0);
            }
            else if(dlist.get(i).length() == 0){
                realBG.add(Float.parseFloat(dlist.get(i-1))); //이전거 저장
            }
            else{
                realBG.add(Float.parseFloat(dlist.get(i)));
            }
        }
        predBG.addAll(realBG);
        predBG.add(Float.parseFloat(dlist.get(4)));

        Log.i("draw","line");
        // 그림그리는 라인 getData로 넣을 값 homeact에서 전달해주고 이메소드 불러주기
        ArrayList<ArrayList<Float>> datalists = new ArrayList<>();
        datalists.add(predBG);
        datalists.add(realBG);
        //get data method
        //draw graph
        if(lineViewFloat == null){
            Log.i("NULL","lineview is null");
        }
        lineViewFloat.setDrawDotLine(true);
        lineViewFloat.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //최대최소값 마커 없앨거면 show_popups_none
        lineViewFloat.setColorArray(new int[]{Color.parseColor("#82BEE6"),Color.parseColor("#FBDD65")});
        lineViewFloat.setBottomTextList(dateTime);
        lineViewFloat.setFloatDataList(datalists);
        Log.i("뿌잉", "onCreate: lineview set완료");
    }

    private ArrayList<Float> loadRealBG() {
        ArrayList<Float> result = new ArrayList<>();
        return result;
    }



}
