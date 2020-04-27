
        package com.example.dankerbell.Firebase;

        import android.util.Log;

        import androidx.annotation.NonNull;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.SetOptions;
        import com.google.firebase.firestore.auth.User;

        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;

public class BloodSugarMapper implements MappingInterface {
    String userId; //사용자 id
    String bsId;
    double bloodsugar; //혈당
    double bloodpressure; //혈압
    double cholesterol; //콜레스테롤
    double hbA1c; //당화혈색소(단위 %)
    Date date; //날짜 시간이랑 분리 할지 말지,,
    String time; //기상 후 식전 식후 ,,,
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();

    public BloodSugarMapper(String userId, double bloodsugar, double bloodpressure, Date date, String time) {
        this.userId = userId;
        this.bloodsugar = bloodsugar;
        this.bloodpressure = bloodpressure;
        this.date = new Date();
        this.time = time;
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("bloodsugar", bloodsugar);
        result.put("bloospressure", bloodpressure);
        result.put("date", date);
        result.put("time", time);
        return result;
    }
}