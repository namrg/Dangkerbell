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

package com.example.dankerbell.bloodManagement;

import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BloodReporter<a> {
    private final HealthDataStore mStore;
    HealthConstants.BloodPressure bm;
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    private BloodObserver bloodObserver;
    String count = "";
    String count2="";
    String count3="";

    public BloodReporter(HealthDataStore store) {
        mStore = store;
    }

    public void start(BloodObserver listener) {
        bloodObserver = listener;
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.BloodPressure.HEALTH_DATA_TYPE, mObserver);
        readTodayblood();
        readblood();
    }

    // Read the today's step count on demand
    private void readTodayblood() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = startTime + ONE_DAY_IN_MILLIS;
        Log.d("날짜", String.valueOf(getStartTimeOfToday()));

        ReadRequest request = new ReadRequest.Builder()
                    .setDataType(HealthConstants.BloodPressure.HEALTH_DATA_TYPE)
                    .setProperties(new String[] {HealthConstants.BloodPressure.DIASTOLIC, HealthConstants.BloodPressure.SYSTOLIC})
                    .setLocalTimeRange(HealthConstants.BloodPressure.START_TIME, HealthConstants.BloodPressure.TIME_OFFSET,
                            startTime, endTime)
                    .build();
        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
            //Log.e(MainActivity.APP_TAG, "Getting step count fails.", e);
        }
    }
    private void readblood() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
       long startTime = getStartTime();
       long endTime = startTime + ONE_DAY_IN_MILLIS;
        Log.d("날짜", String.valueOf(getStartTimeOfToday()));

        ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.BloodPressure.HEALTH_DATA_TYPE)
                .setProperties(new String[] {HealthConstants.BloodPressure.DIASTOLIC, HealthConstants.BloodPressure.SYSTOLIC})
                .setLocalTimeRange(HealthConstants.BloodPressure.START_TIME, HealthConstants.BloodPressure.TIME_OFFSET,
                        startTime, endTime)
                .build();



        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
            //Log.e(MainActivity.APP_TAG, "Getting step count fails.", e);
        }
    }


    public long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
       // Log.d("날짜", String.valueOf(today.getTime()));

        return today.getTimeInMillis();
    }
    public long getStartTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        // final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        //final String date = sdf.format(calendar.getTime());
        return calendar.getTimeInMillis();    }
    Date a=getStartTimeOfToda2();
    public long getStartTimeprev(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌
        return calendar.getTimeInMillis();
    }
    public long getStartTimenext(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        calendar.add(Calendar.DATE, +1);  // 오늘 날짜에서 하루를 뺌
        return calendar.getTimeInMillis();
    }


    public Date getStartTimeOfToda2() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        SimpleDateFormat f = new SimpleDateFormat("yy.MM.dd.HH:mm");
        f.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        final String date = f.format(today.getTime());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Log.d("날짜1", String.valueOf(today.getTime()));
        Log.d("날짜2", String.valueOf(date));

        return today.getTime();
    }
    public final HealthResultHolder.ResultListener<ReadResult> mListener = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {

            try {
                for (HealthData data : result) {
                    count = data.getString(HealthConstants.BloodPressure.DIASTOLIC);
                    count2 = data.getString(HealthConstants.BloodPressure.SYSTOLIC);
                   // count3= data.getString(HealthConstants.BloodPressure.START_TIME);
                    Log.d("수축기", count);
                    Log.d("이완기", count2);
                   // Log.d("날짜", count3);

                }
            } finally {
                result.close();
            }

//        if (mObserver != null) {
//            mObserver.onChanged(count);
//        }
        }
    };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            //Log.d(MainActivity.APP_TAG, "Observer receives a data changed event");
            readTodayblood();
        }
    };


    public interface BloodObserver {
        void onChanged(String count);
    }
}
