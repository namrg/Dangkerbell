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

import java.util.Calendar;
import java.util.TimeZone;

public class glucoseReporter {
    private final HealthDataStore mStore;
    private static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L;
    private BloodglucoseObserver bloodglucoseObserver;
    String count = "";
    String count2="";
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
            Log.e(bloodActivity.APP_TAG, "Getting step count fails.", e);
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
                count = data.getString(HealthConstants.BloodGlucose.GLUCOSE);

                Log.d(this.getClass().getName(),count);
                Log.d(this.getClass().getName(),count);

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
            Log.d(bloodActivity.APP_TAG, "Observer receives a data changed event");
            readTodayblood();
        }
    };


    public interface BloodglucoseObserver {
        void onChanged(String count);
    }
}
