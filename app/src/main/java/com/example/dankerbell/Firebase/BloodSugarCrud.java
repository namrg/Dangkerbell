
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
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.HashMap;
        import java.util.Map;

        //import static com.facebook.FacebookSdk.getApplicationContext;

        public class BloodSugarCrud extends AppCompatActivity implements CrudInterface  {
    private static BloodSugarCrud instance; //싱글톤
    public String bloodsugar=" ";
    public String bloodpressure=" ";
    public String mbloodsugar=" ";
    public String mbloodpressure=" ";
    public String lbloodsugar=" ";
    public String lbloodpressure=" ";
    public String dbloodsugar=" ";
    public String dbloodpressure=" ";
    public String sbloodsugar=" ";
    public String sbloodpressure=" ";
    public static Handler mHandler1 =new Handler();

            public String getMbloodsugar() {
                return mbloodsugar;
            }

            public String getMbloodpressure() {
                return mbloodpressure;
            }

            public String getLbloodsugar() {
                return lbloodsugar;
            }

            public String getLbloodpressure() {
                return lbloodpressure;
            }

            public String getDbloodsugar() {
                return dbloodsugar;
            }

            public String getDbloodpressure() {
                return dbloodpressure;
            }

            public String getSbloodsugar() {
                return sbloodsugar;
            }

            public String getSbloodpressure() {
                return sbloodpressure;
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


    public void create(double bloodsugar, double bloodpressure, String date, String time) {
        //오버로딩
        Map<String, Object> updateData = new HashMap<>();

        //if(add){
        BloodSugarMapper post = new BloodSugarMapper(bloodsugar, bloodpressure, date, time);
        updateData = post.toMap();
        //}
//        db.collection(User)
//        .add(updateData)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//        @Override
//        public void onSuccess(DocumentReference documentReference) {
//            Log.d("혈당 데이터 추가", "DocumentSnapshot added with ID: " + documentReference.getId());
//        }
//    })
//            .addOnFailureListener(new OnFailureListener() {
//        @Override
//        public void onFailure(@NonNull Exception e) {
//            Log.w("혈당 데이터 추가", "Error adding document", e);
//        }
//    });

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



    public void wakeupread(final String date){
        db.collection("user").document(User).collection("wakeupbloodSugar").document(date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        bloodsugar=document.getData().get("bloodsugar").toString();
                        bloodpressure=document.getData().get("bloodpressure").toString();
                    }
                    else{ bloodsugar="";
                          bloodpressure="";}
                    Log.d("받아오는 혈당",bloodsugar);
                    Log.d("날짜",date);
                    mHandler1.sendEmptyMessage(1000);
                }
            }
        });
    }
            public void morningread(final String date){
                db.collection("user").document(User).collection("morningbloodSugar").document(date)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        mbloodsugar=document.getData().get("bloodsugar").toString();
                                        mbloodpressure=document.getData().get("bloodpressure").toString();
                                    }
                                    else{ mbloodsugar="";
                                        mbloodpressure="";}
                                    Log.d("받아오는 혈당",mbloodsugar);
                                    Log.d("날짜",date);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }

            public void lunchread(final String date){
                db.collection("user").document(User).collection("lunchbloodSugar").document(date)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        lbloodsugar=document.getData().get("bloodsugar").toString();
                                        lbloodpressure=document.getData().get("bloodpressure").toString();
                                    }
                                    else{ lbloodsugar="";
                                        lbloodpressure="";}
                                    Log.d("받아오는 혈당",lbloodsugar);
                                    Log.d("날짜",date);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }
            public void dinnerread(final String date){
                db.collection("user").document(User).collection("dinnerbloodSugar").document(date)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        dbloodsugar=document.getData().get("bloodsugar").toString();
                                        dbloodpressure=document.getData().get("bloodpressure").toString();
                                    }
                                    else{ dbloodsugar="";
                                        dbloodpressure="";}
                                    Log.d("받아오는 혈당",dbloodsugar);
                                    Log.d("날짜",date);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }
            public void sleepread(final String date){
                db.collection("user").document(User).collection("sleepbloodSugar").document(date)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        sbloodsugar=document.getData().get("bloodsugar").toString();
                                        sbloodpressure=document.getData().get("bloodpressure").toString();
                                    }
                                    else{ sbloodsugar="";
                                        sbloodpressure="";}
                                    Log.d("받아오는 혈당",sbloodsugar);
                                    Log.d("날짜",date);
                                    mHandler1.sendEmptyMessage(1000);
                                }
                            }
                        });
            }


            public void morning(final String date){//
                db.collection(User).document("morningbloodSugar"+date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                mbloodpressure=document.getData().get("bloodpressure").toString();
                                mbloodsugar=document.getData().get("bloodsugar").toString();}
                            else{
                                mbloodpressure="";
                                mbloodsugar="";
                            }
                            Log.d("받아오는 혈압",mbloodpressure);
                            Log.d("날짜",date);
                            mHandler1.sendEmptyMessage(1000);
                        }

                    }
                });
            }
            public String getmBloodpressure(){
                return mbloodpressure;
            }


            public String getmBloodsugar(){
                return mbloodsugar;
            }

    public String getBloodpressure(){
                return bloodpressure;
            }


    public String getBloodsugar(){
        return bloodsugar;
    }



    @Override
    public void update() {
//

}

    @Override
    public void delete() {
    }
}