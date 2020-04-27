
        package com.example.dankerbell.Firebase;

        import android.util.Log;


        import androidx.annotation.NonNull;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;

public class BloodSugarCrud implements CrudInterface {
    private static BloodSugarCrud instance; //싱글톤
    //FirebaseFirestore db
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    public static BloodSugarCrud getInstance() {
        if(instance == null){
            instance = new BloodSugarCrud();
        }
        return instance;
    }

    @Override
    public void create(){}

    public void create(String userId, double bloodsugar, double bloodpressure, Date date, String time) {
        //오버로딩
        Map<String, Object> updateData = new HashMap<>();

        //if(add){
        BloodSugarMapper post = new BloodSugarMapper(userId, bloodsugar, bloodpressure, date, time);
        updateData = post.toMap();
        //}
        //추가한부분
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

        db.collection(User)
        .add(updateData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Log.d("혈당 데이터 추가", "DocumentSnapshot added with ID: " + documentReference.getId());
        }
    })
            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.w("혈당 데이터 추가", "Error adding document", e);
        }
    });
    }


    @Override
    public void read() {
        db.collection("bloodSugarDB")
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