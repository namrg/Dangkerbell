package com.example.dankerbell;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class PutData {
    static ArrayList<BGjson> datalist;
    public PutData(Context context){
        Log.i(this.getClass().getName(), "putdata 들어옴" );
        Gson gson = new Gson();
        datalist = new ArrayList<>();
        BloodSugarCrud bsc = BloodSugarCrud.getInstance();
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream is = context.getAssets().open("f_bgl.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            String line = reader.readLine();

            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }
        }catch (Exception e){
        }

            Log.i(this.getClass().getName(), "json 읽음" );
            String json = buffer.toString();
            Log.i(this.getClass().getName(), "1" );
//            JSONObject jsonObject = new JSONObject(json);
//            JSONArray jsonArray = jsonObject.getJSONArray("json");
//            Log.i(this.getClass().getName(), "2" );
//            int index = 0;
//            while (index < jsonArray.length()) {
//                Log.i(this.getClass().getName(), "3" );
//                BGjson bGjson = gson.fromJson(jsonArray.get(index).toString(), BGjson.class);
//                Log.i(this.getClass().getName(), "1" );
//                datalist.add(bGjson);
//                index++;
//            }
            Log.i(this.getClass().getName(), "2" );
            JsonParser jsonParser = new JsonParser();
            Log.i(this.getClass().getName(), "2.5" );
            JsonArray jsonArray = (JsonArray) jsonParser.parse(json);
            Log.i(this.getClass().getName(), "3" );
            for(JsonElement o : jsonArray){
                JsonObject obj = o.getAsJsonObject();
                Log.i(this.getClass().getName(), obj.toString() );
                BGjson bGjson = new BGjson();
                Log.i(this.getClass().getName(), "??"+obj.get("dateTime").toString());
                bGjson.setDateTime(obj.get("dateTime").getAsString());
                Log.i(this.getClass().getName(), bGjson.dateTime );
                bGjson.bloodglucose = obj.get("bloodglucose").getAsDouble();
                Log.i(this.getClass().getName(), String.valueOf(bGjson.bloodglucose));
                bGjson.UltralenteInsuline = obj.get("UltralenteInsuline").getAsDouble();
                bGjson.NPHInsuline = obj.get("NPHInsuline").getAsDouble();
                bGjson.regularInsuline = obj.get("regularInsuline").getAsDouble();
                //public void create(double bloodsugar, double regular,double NPH,double Ultra,String date, String time)
                bsc.create(bGjson.bloodglucose, bGjson.regularInsuline, bGjson.NPHInsuline, bGjson.UltralenteInsuline, bGjson.dateTime, bGjson.month,bGjson.day,bGjson.time);
                Log.i(this.getClass().getName(), "------create 완료------" );
                datalist.add(bGjson);
            }
            Log.i(this.getClass().getName(), "------전부 읽고 객체 생성 및 create 완료------" );
//        }catch (Exception e){
//            Log.e(this.getClass().toString(), "안되는거야?"+String.valueOf(e));
//        }
    }
}

class BGjson{
    //double bloodsugar, double regular,double NPH,double Ultra,String date, String time
    String dateTime="";
    String year = "";//'2020'
    String month = "";//'01'
    String day = "";

    double regularInsuline;
    double NPHInsuline ;
    double UltralenteInsuline;
    double bloodglucose;
    String time;

    public void setDateTime(String input){
        dateTime = input;
        String[] dt = input.split(" ");
        String d = dt[0];//'2020-01-14'
        String t = dt[1];//'22:00:00'
        String[] dt2 = d.split("-");
        year = dt2[0];//'2020'
        month = dt2[1];//'01'
        day = dt2[2];//'14'

        if(t.equals("08:00:00")){
            time = "기상후";
        }
        else if(t.equals("12:00:00")){
            time = "점심";
        }
        else if(t.equals("18:00:00")){
            time = "저녁";
        }
        else{
            time = "취침전";
        }
    }

    @Override
    public String toString() {
        return "user{ rnjsgpwjd5926@gmail.com{ bloodsugar{ " +

                month + "{"+ day + "{" +

                "dateTime'" + dateTime + '\'' +

                ", regularInsuline='" + regularInsuline + '\'' +

                ", NPHInsuline='" + NPHInsuline + '\'' +

                ", UltralenteInsuline='" + UltralenteInsuline + '\'' +

                ", time ='" + time + '\'' +

                ", bloodglucose=" + bloodglucose + '\'' +

                "}}}}}";

    }
}