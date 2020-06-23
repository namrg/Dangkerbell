package com.example.dankerbell;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import java.util.ArrayList;

import static com.example.dankerbell.homeActivity.blood;


public class  ForecastingBG {
    static Activity activity;
      public static Handler fbgHandler =new Handler();
    private static final String model_path = "model.tflite";
    FirebaseCustomLocalModel model; //tflite 모델
    FirebaseModelInterpreter interpreter;
    FirebaseModelInputOutputOptions inputOutputOptions;
    FirebaseModelInputs inputs;
    float[][] input, output;
    float prediction;

    public ForecastingBG() throws FirebaseMLException {
        /*
        입출력 지정
        input shape = [1 7]
        input dtype = float
        output shape = [1 1]
        output dtype = float
         */
        input = new float[][]{{0,0,0,0,0,0,0}}; //regIns, NPHIns, UltraIns, Hypo, Meal, Exercise, bgl
        output = new float[][]{{1}};

        //입력값
        //homeactivity에서 당일의 혈당만을 읽어옴
        homeActivity h = new homeActivity();
        ArrayList<String> dlist = h.blood; //home activity의 blood static arraylist에 있음

        for(String a: blood){
            Log.e("blood가 가진 값","|"+ a+"|");
        }

        //입력값
//        input[0][0] = ;
//        input[0][1] = ;
//        input[0][2] = ;
//        input[0][3] = ;
//        input[0][4] = ;
//        input[0][5] = ;
        try {
            input[0][input[0].length - 1] = Float.parseFloat(blood.get(blood.size() - 1)); //혈당 값 저장
        }catch (Exception e){
            Log.e("blood is null","!");
            //어제값으로 저장..
            //input[0][input[0].length - 1] = Float.parseFloat(blood.get(blood.size() - 1)); //혈당 값 저장
        }

        //모델 로드
        model = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath(model_path)
                .build();
        //인터프리터
        interpreter = getTfliteInterpreter();

        //입출력 구성
        inputOutputOptions =
                new FirebaseModelInputOutputOptions.Builder()
                        .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 7})
                        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 1})
                        .build(); //입출력 shape

        inputs = new FirebaseModelInputs.Builder()
                .add(input)  // add() as many input arrays as your model requires
                .build();

        if(input == null || output == null){
            Log.i(getClass().getName(),"input / output is null");
        }

    }

    //모델실행
    synchronized void runModel(){
        interpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                output = result.getOutput(0);
                                float[] probabilities = output[0];
                                setPrediction(output[0][0]);
                                Log.i(getClass().getName(), "Success run model");
                                Log.i(getClass().getName(), String.valueOf("출력결과 :"+ String.valueOf(output[0][0])));
                                Log.i(getClass().getName(), String.valueOf("getprediction :"+ getPrediction()));
                                fbgHandler.sendEmptyMessage(1000);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(getClass().getName(), "Fail to run model : "+e.getMessage());
                                fbgHandler.sendEmptyMessage(2000);
                            }
                        });
        //output[0][0];//예측 결과
    }
    synchronized void setPrediction(float val){
        this.prediction = val;
        Log.e(getClass().getName(), "setprediction : "+ this.prediction);

    }
    synchronized float getPrediction(){
        return prediction;
    }

    private FirebaseModelInterpreter getTfliteInterpreter() {
        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(model).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
        return interpreter;
    }

}
