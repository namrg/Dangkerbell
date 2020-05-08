
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

        import static com.facebook.FacebookSdk.getApplicationContext;

        public class BloodSugarCrud extends AppCompatActivity implements CrudInterface  {
    private static BloodSugarCrud instance; //싱글톤
    public String bloodsugar=" ";
    public String bloodpressure=" ";
    public String mbloodsugar=" ";
    public String mbloodpressure=" ";
    public static Handler mHandler1 =new Handler();


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
        db.collection(User)
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
    public void readequltodate(final String date){ // 실행 O 값 전달어떻게?
        db.collection(User)
                .whereEqualTo("time","기상 후")
                .whereEqualTo("date",date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d("날짜가 같은 혈당 데이터 읽기",document.getId() + " => " + document.getData().get("bloodsugar"));

                                Log.d("날짜",date);

                            }
                        }
                        else {
                            Log.w("날짜가 같은 혈당 데이터 읽기", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void wakeupbloodSugar(final String date){
//            db.collection("user").document(User).collection("morningbloodSugar").document(date).set(result);
        db.collection("user").document(User).collection("morningbloodSugar").document(date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        bloodsugar=document.getData().get("bloodsugar").toString();}
                    else bloodsugar="";
                    Log.d("받아오는 혈당",bloodsugar);
                    Log.d("날짜",date);
                    mHandler1.sendEmptyMessage(1000);
                }
            }
        });
    }
    public void wakeupbloodPressure(final String date){// 실행 O 값 전달 O
        db.collection(User).document("wakeupbloodSugar"+date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists())
                        bloodpressure=document.getData().get("bloodpressure").toString();
                    else
                        bloodpressure="";
                    Log.d("받아오는 혈압",bloodpressure);
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
//           //오버로딩
//        Map<String, Object> updateData = new HashMap<>();
//
//        //if(add){
//        BloodSugarMapper post = new BloodSugarMapper(userId, bloodsugar, bloodpressure, date, time);
//        updateData = post.toMap();
//        db.collection(User).document("bloodSugarDB")
//                .set(updateData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("set혈당데이터", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("set혈당데이터", "Error writing document", e);
//                    }
//                });

}

    @Override
    public void delete() {
        //혈당 기록은 삭제 기능이 없음
    }
}