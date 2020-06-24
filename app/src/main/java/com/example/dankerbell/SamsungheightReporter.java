/**
 * Copyright (C) 2014 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

package com.example.dankerbell;

import android.os.Handler;
import android.util.Log;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.Firebase.profileCrud;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.util.Calendar;
import java.util.TimeZone;

public class SamsungheightReporter {
    private final HealthDataStore mStore;
    BloodSugarCrud mBloodSugar = BloodSugarCrud.getInstance(); //firebase 참조 singletone
    public static Handler bHandler =new Handler();
    profileCrud mprofile = profileCrud.getInstance();

    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    private HeightObserver heightObserver;
    static String count;
    static Double count2;
    static float count3;
    Double height;
    Double weight;
    Double mbmi;
    public SamsungheightReporter(HealthDataStore store) {
        mStore = store;
    }

    public void start(HeightObserver listener) {
        heightObserver = listener;
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.Height.HEALTH_DATA_TYPE, mObserver);
        Log.d("키 실행 안돼1!!!!!","키 왜실행앙대?");
        readTodayheight();
    }

    // Read the today's step count on demand
    private void readTodayheight() {
        Log.d("키 실행 안돼1!!!!!","readTodayheighy");

        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = startTime + ONE_DAY_IN_MILLIS;

        ReadRequest request = new ReadRequest.Builder()
                    .setDataType(HealthConstants.Height.HEALTH_DATA_TYPE)
                    .setProperties(new String[] {HealthConstants.Height.HEIGHT})
//                    .setLocalTimeRange(HealthConstants.Height.START_TIME, HealthConstants.Height.TIME_OFFSET,
//                            startTime, endTime)
                    .build();

        try {
            resolver.read(request).setResultListener(mListener);
            Log.d("키 실행 안돼1!!!!!2222","ㅜㅜ");

        } catch (Exception e) {
            //Log.e(bloodActivity.APP_TAG, "Getting height fails.", e);
        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListener = result -> {

        try {
            for (HealthData data : result) {
                count = data.getString(HealthConstants.Height.HEIGHT);
                Log.d("키",String.valueOf(count));
                Log.d("몸무게",String.valueOf(SamsungweightReporter.count));
//                height=Double.parseDouble(count);
//                weight=Double.parseDouble(SamsungweightReporter.count);
//                    Double iheight2=(height)/100;
//                    mbmi=weight/(iheight2*iheight2);
//                    mbmi=Double.parseDouble(String.format("%.2f",mbmi));
                height=Double.parseDouble(count);
                weight=Double.parseDouble(SamsungweightReporter.count);
                Double iheight2=(height)/100;
                mbmi=weight/(iheight2*iheight2);
                mbmi=Double.parseDouble(String.format("%.2f",mbmi));
                Log.d("weight",String.valueOf(weight));
                mprofile.updatehw(count,SamsungweightReporter.count,mbmi);
                if(mprofile.getMygender().length()==0){
                    mprofile.createprofile("","","",height,weight,mbmi,"","","","","","","");

                }
                else{
                 mprofile.updatehw(count,SamsungweightReporter.count,mbmi);
                }

//                if(mprofile.getMybirthday().equals("")){
//
//
//                //만약에 입력을 안한애면 키랑,몸무게 insert
//                //입력된 애면 update
//            }
//                else{
//
//                }
                          }
        } finally {
            result.close();
        }

//        if (mObserver != null) {
//            mObserver.onChanged(count);
//        }
    };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
           //Log.d(bloodActivity.APP_TAG, "Observer receives a data changed event");
            readTodayheight();
        }
    };


    public interface HeightObserver {
        void onChanged(String count);
    }
}
