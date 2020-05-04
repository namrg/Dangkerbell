
        package com.example.dankerbell.Firebase;

        import android.util.Log;

        import androidx.annotation.NonNull;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.SetOptions;
        import com.google.firebase.firestore.auth.User;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Locale;
        import java.util.Map;

public class BloodSugarMapper implements MappingInterface {
    String userId; //사용자 id
    String bsId;
    double bloodsugar; //혈당
    double bloodpressure; //혈압
    double cholesterol; //콜레스테롤
    double hbA1c; //당화혈색소(단위 %)
   // Date date; //날짜 시간이랑 분리 할지 말지,,
    String date;
    String time; //기상 후 식전 식후 ,,,
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    CollectionReference bloodSugarDB = db.collection(User);
    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
    final Calendar calendar = Calendar.getInstance(); // 오늘날짜
    final String day = sdf.format(calendar.getTime());

  //  public BloodSugarMapper(String userId, double bloodsugar, double bloodpressure, Date date, String time) {
  public BloodSugarMapper(double bloodsugar, double bloodpressure, String date, String time) {

        this.bloodsugar = bloodsugar;
        this.bloodpressure = bloodpressure;
        //this.date = new Date();
        this.date=date;
        this.time = time;
    }


    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
       // result.put("userId", userId);
        result.put("bloodsugar", bloodsugar);
        result.put("bloodpressure", bloodpressure);
        result.put("date", date);
        result.put("time", time);
        if((time == "아침")) { // 날짜가 오늘 날짜인 것 추가해야함
            Log.d("date", date);
            Log.d("day", day);
          //  db.collection(User).document("morningbloodSugar"+date).set(result);
            db.collection("user").document(User).collection("morningbloodSugar").document(date).set(result);
        }
        else if(time=="기상 후"){
          //  db.collection(User).document("wakeupbloodSugar"+date).set(result);
            db.collection("user").document(User).collection("wakeupbloodSugar").document(date).set(result);

        }
        else if(time=="점심"){
            db.collection(User).document("lunchbloodSugar"+date).set(result);
        }
        else if(time=="저녁"){
            db.collection(User).document("dinnerbloodSugar"+date).set(result);
        }
        else{
            db.collection(User).document("sleepbloodSugar"+date).set(result);
        }
        return result;
    }
}