package com.example.dankerbell;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ForecastingBG {
    static Activity activity;
    private static final String MODEL_PATH = "converted_model.tflite";
    protected Interpreter tflite;

    public ForecastingBG(){
        double[] input = new double[]{};
        double[] output = new double[]{};

        tflite = getTfliteInterpreter(getModelPath());
        tflite.run(input, output);

        //output[0];//예측 결과
    }

    protected String getModelPath(){
        return MODEL_PATH;
    }
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(activity, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
