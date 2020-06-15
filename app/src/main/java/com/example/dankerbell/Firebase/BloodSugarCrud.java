
        package com.example.dankerbell.Firebase;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.util.Log;
        import android.widget.TextView;
        import android.widget.Toast;


        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.example.dankerbell.R;
        import com.example.dankerbell.bloodManagement.bloodActivity;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.google.firebase.firestore.SetOptions;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Locale;
        import java.util.Map;

        //import static com.facebook.FacebookSdk.getApplicationContext;

        public class BloodSugarCrud extends AppCompatActivity implements CrudInterface  {
    private static BloodSugarCrud instance; //싱글톤
    String bloodsugar=" ";
    String wregular=" ";
    String wultra=" ";
    String wNPH=" ";

            public String getBloodsugar() {
                return bloodsugar;
            }

            String mbloodsugar=" ";
    String mregular="";
    String multra="";
    String mNPH="";

    String lbloodsugar=" ";
    String lregular=" ";
    String lultra=" ";
    String lNPH=" ";

    String dbloodsugar=" ";
    String dregular=" ";
    String dultra=" ";
    String dNPH=" ";

    String sbloodsugar=" ";
    String sregular=" ";
 String sultra=" ";
    String sNPH=" ";

            public static Handler mHandler1 =new Handler();

            public String getMbloodsugar() {
                return mbloodsugar;
            }

            public String getlbloodsugar() {
                return lbloodsugar;
            }


            public String getdbloodsugar() {
                return dbloodsugar;
            }


            public String getsbloodsugar() {
                return sbloodsugar;
            }

            public String getWregular() {
                return wregular;
            }

            public String getWultra() {
                return wultra;
            }

            public String getwNPH() {
                return wNPH;
            }

            public String getMregular() {
                return mregular;
            }

            public String getMultra() {
                return multra;
            }

            public String getmNPH() {
                return mNPH;
            }

            public String getLregular() {
                return lregular;
            }

            public String getLultra() {
                return lultra;
            }

            public String getlNPH() {
                return lNPH;
            }

            public String getDregular() {
                return dregular;
            }

            public String getDultra() {
                return dultra;
            }

            public String getdNPH() {
                return dNPH;
            }

            public String getSregular() {
                return sregular;
            }

            public String getSultra() {
                return sultra;
            }

            public String getsNPH() {
                return sNPH;
            }

            //FirebaseFirestore db
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    public static BloodSugarCrud getInstance() {
        if(instance == null){
            instance = new BloodSugarCrud();
        }
        return instance;
    }
            SimpleDateFormat monthformat = new SimpleDateFormat("MM", Locale.getDefault());
            SimpleDateFormat monthofdayformat = new SimpleDateFormat("dd", Locale.getDefault());

            final Calendar calendar = Calendar.getInstance(); // 오늘날짜
            public final String day = monthofdayformat.format(calendar.getTime());
            public String month=monthformat.format(calendar.getTime());

    public void create(double bloodsugar, double regular,double NPH,double Ultra,String date, String time) {
        //오버로딩
        Map<String, Object> updateData = new HashMap<>();

        //if(add){
        BloodSugarMapper post = new BloodSugarMapper(bloodsugar, regular, NPH,Ultra,date, time);
        updateData = post.toMap();
        //}
        db.collection("user").document(User).collection("bloodsugar").document(month).collection(day).document(time)
                .set(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("데이터 추가", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("데이터 추가", "Error writing document", e);
                    }
                });
    }

            public void create(double bloodsugar, double regular,double NPH,double Ultra,String date,String month, String day, String time) {
                //오버로딩
                Map<String, Object> updateData = new HashMap<>();

                //if(add){
                BloodSugarMapper post = new BloodSugarMapper(bloodsugar, regular, NPH,Ultra,date, time);
                updateData = post.toMap();
                //}
                db.collection("user").document(User).collection("bloodsugar").document(month).collection(day).document(time)
                        .set(updateData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("데이터 추가", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("데이터 추가", "Error writing document", e);
                            }
                        });
            }

    @Override
    public void create() {

    }

    @Override
    public void read() {
        db.collection("user").document(User).collection("wakeupbloodSugar")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("혈당 데이터 읽기", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("혈당 데이터 읽기", "Error getting documents.", task.getException());
                        }
                    }
                });
    }



    public void wakeupread(String m,String d){
        db.collection("user").document(User).collection("bloodsugar").document(m).collection(d).document("기상후")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        bloodsugar=document.getData().get("bloodglucose").toString();
                        wregular=document.getData().get("regularInsuline").toString();
                        wNPH=document.getData().get("NPHInsuline").toString();
                        wultra=document.getData().get("UltralenteInsuline").toString();
                        Log.d("받아오는 혈당",bloodsugar);

                    }
                    else{ bloodsugar="";
                          wregular="";
                          wNPH="";
                          wultra="";
                    }
                    Log.d("받아오는 혈당",bloodsugar);
                    mHandler1.sendEmptyMessage(1000);
                }
            }
        });
    }
            public void morningread(String m,String d){
                db.collection("user").document(User).collection("bloodsugar").document(m).collection(d).document("아침")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        mbloodsugar=document.getData().get("bloodglucose").toString();
                                        if(document.getData().get("regularInsuline").toString().equals("1.0")){
                                           mregular="";
                                        }
                                        if(document.getData().get("NPHInsuline").toString().equals("1.0")){
                                            mNPH="";
                                        }
                                        else
                                            mNPH=document.getData().get("NPHInsuline").toString();

                                        if(document.getData().get("UltralenteInsuline").toString().equals("1.0")){
                                            multra="";
                                        }
                                        else
                                            multra=document.getData().get("UltralenteInsuline").toString();


                                    }
                                    else{ mbloodsugar="";

                                        mregular="";
                                        mNPH="";
                                        multra="";}
                                    Log.d("받아오는 혈당",mbloodsugar);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }

            public void lunchread(String m,String d){
                db.collection("user").document(User).collection("bloodsugar").document(m).collection(d).document("점심")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        lbloodsugar=document.getData().get("bloodglucose").toString();
                                        lregular=document.getData().get("regularInsuline").toString();
                                        lNPH=document.getData().get("NPHInsuline").toString();
                                        lultra=document.getData().get("UltralenteInsuline").toString();

                                    }
                                    else{ lbloodsugar="";
                                        lregular="";
                                        lNPH="";
                                        lultra="";}
                                    Log.d("받아오는 혈당",lbloodsugar);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }
            public void dinnerread(String m,String d){
                db.collection("user").document(User).collection("bloodsugar").document(m).collection(d).document("저녁")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        dbloodsugar=document.getData().get("bloodglucose").toString();
                                        dregular=document.getData().get("regularInsuline").toString();
                                        dNPH=document.getData().get("NPHInsuline").toString();
                                        dultra=document.getData().get("UltralenteInsuline").toString();
                                    }
                                    else{ dbloodsugar="";
                                        dregular="";
                                        dNPH="";
                                        dultra="";
                                    }
                                    Log.d("받아오는 혈당",dbloodsugar);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }
            public void sleepread(String m,String d){
                db.collection("user").document(User).collection("bloodsugar").document(m).collection(d).document("취침전")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        sbloodsugar=document.getData().get("bloodglucose").toString();
                                        sregular=document.getData().get("regularInsuline").toString();
                                        sNPH=document.getData().get("NPHInsuline").toString();
                                        sultra=document.getData().get("UltralenteInsuline").toString();
                                    }
                                    else{
                                        sbloodsugar="";
                                        sregular="";
                                        sNPH="";
                                        sultra="";
                                    }
                                    Log.d("받아오는 혈당",sbloodsugar);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }


//            public void morning(final String date){//
//                db.collection(User).document("morningbloodSugar"+date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            DocumentSnapshot document = task.getResult();
//                            if(document.exists()){
//                                mbloodpressure=document.getData().get("bloodpressure").toString();
//                                mbloodsugar=document.getData().get("bloodsugar").toString();}
//                            else{
//                                mbloodpressure="";
//                                mbloodsugar="";
//                            }
//                            Log.d("받아오는 혈압",mbloodpressure);
//                            Log.d("날짜",date);
//                            mHandler1.sendEmptyMessage(1000);
//                        }
//
//                    }
//                });
//            }




            public void updateglucose(Double glucose,String m,String d,String date) {
                Log.d("혈당 ","혈당업뎃찡 !!!!!");
                // Update one field, creating the document if it does not already exist.
                Map<String, Object> data = new HashMap<>();
                data.put("bloodglucose", glucose);
                data.put("dateTime", date);
                db.collection("user").document(User).collection("bloodsugar").document(m).collection(d).document("아침")
                        .set(data, SetOptions.merge());
            }




    @Override
    public void update() {
//

}

    @Override
    public void delete() {
    }
}