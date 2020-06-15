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

import android.os.Handler;
import android.util.Log;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.Firebase.timeCrud;
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
import java.util.Locale;
import java.util.TimeZone;

public class glucoseReporter {
    private final HealthDataStore mStore;
    BloodSugarCrud mBloodSugar = BloodSugarCrud.getInstance(); //firebase 참조 singletone
    public static Handler bHandler =new Handler();

    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    private BloodglucoseObserver bloodglucoseObserver;
    static String count = "";
    public glucoseReporter(HealthDataStore store) {
        mStore = store;
    }

    public void start(BloodglucoseObserver listener) {
        bloodglucoseObserver = listener;
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, mObserver);
        readTodayblood();
    }

    // Read the today's step count on demand
    private void readTodayblood() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = startTime + ONE_DAY_IN_MILLIS;

        ReadRequest request = new ReadRequest.Builder()
                    .setDataType(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE)
                    .setProperties(new String[] {HealthConstants.BloodGlucose.GLUCOSE})
                    .setLocalTimeRange(HealthConstants.BloodGlucose.START_TIME, HealthConstants.BloodGlucose.TIME_OFFSET,
                            startTime, endTime)
                    .build();

        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
          //  Log.e(bloodActivity.APP_TAG, "Getting step count fails.", e);
        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListener = result -> {
        final SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        final SimpleDateFormat timeinclude = new SimpleDateFormat("yy-MM-dd HH:MM", Locale.getDefault());
        SimpleDateFormat monthformat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat monthofdayformat = new SimpleDateFormat("dd", Locale.getDefault());

        final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String day = monthofdayformat.format(calendar.getTime());
        String month=monthformat.format(calendar.getTime());

        final String timeminutedate=timeinclude.format(calendar.getTime());


        final String date = sdf.format(calendar.getTime());
        try {
            for (HealthData data : result) {
                count = data.getString(HealthConstants.BloodGlucose.GLUCOSE);

                Log.d("혈당",count);
                Log.d(this.getClass().getName(),count);
                Double glu=Double.parseDouble(count);
                glu=Double.parseDouble(String.format("%.2f",glu));
                glu=glu*18;
                count=String.valueOf(Math.round(glu));
                glu=Double.parseDouble(count);

                //count=String.format("%.2f",glu);
                Log.d("혈당2",count);
                mBloodSugar.updateglucose(glu,month,day,date);
                mBloodSugar.create(glu,1.0,1.0,1.0,date,"아침");
                bHandler.sendEmptyMessage(1009);


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
          //  Log.d(bloodActivity.APP_TAG, "Observer receives a data changed event");
            readTodayblood();
        }
    };


    public interface BloodglucoseObserver {
        void onChanged(String count);
    }
}
