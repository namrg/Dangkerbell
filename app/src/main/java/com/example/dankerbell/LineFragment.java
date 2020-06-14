package com.example.dankerbell;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;


import java.util.ArrayList;

import im.dacer.androidcharts.LineView;

public class LineFragment extends Fragment {

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line, container, false);
        final LineView lineViewFloat = (LineView) rootView.findViewById(R.id.line_view_float);

        initLineView(lineViewFloat);
        return rootView;
    }

    private void initLineView(LineView lineView) {
        //label
        ArrayList<String> dateTime = new ArrayList<String>();
        dateTime.add("기상 후");
        dateTime.add("아침");
        dateTime.add("점심");
        dateTime.add("저녁");
        dateTime.add("취침 전");
        //2datasets
        ArrayList<Float> realBG = new ArrayList<>();
        realBG.add((float) 281);
        realBG.add((float) 209);
        realBG.add((float) 421);
        realBG.add((float) 321);
        ArrayList<Float> predBG = new ArrayList<>();
        predBG.add((float) 281);
        predBG.add((float) 209);
        predBG.add((float) 421);

        ArrayList<ArrayList<Float>> datalists = new ArrayList<>();
        datalists.add(realBG);
        datalists.add(predBG);
        //get data method
        //draw graph
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //최대최소값 마커 없앨거면 show_popups_none
        lineView.setColorArray(new int[]{Color.parseColor("#82BEE6"),Color.parseColor("#FBDD65")});
        lineView.setBottomTextList(dateTime);
        lineView.setFloatDataList(datalists);
        Log.i(getActivity().getLocalClassName(), "onCreate: lineview set완료");
    }


}
