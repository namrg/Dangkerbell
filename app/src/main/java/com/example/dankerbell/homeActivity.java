package com.example.dankerbell;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.FragmentManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dankerbell.Bluetooth.ConnectBluetoothActivity;
//import com.example.dankerbell.Firebase.StepCountCrud;
import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.Firebase.StepCountCrud;
import com.example.dankerbell.Firebase.profileCrud;
import com.example.dankerbell.bloodManagement.BloodReporter;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.bloodManagement.glucoseReporter;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.pillManagement.pillActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.opencensus.internal.StringUtils;

public class homeActivity extends AppCompatActivity { // 홈화면 클래스 userid 및 걸음수
    bloodActivity bloodActivity=new bloodActivity();
    profileCrud mprofile=profileCrud.getInstance();
   BloodSugarCrud mbloodsugar=BloodSugarCrud.getInstance();

    String date=bloodActivity.getDate();
    TextView today;
    TextView profile;
    Button blood_btn,meal_btn,pill_btn;
    TextView bluetooth;
    TextView toolbar;
    TextView close;
    DrawerLayout drawerLayout;
    View drawerView;
    Button logout,settime;
    Button mypage;
    SimpleDateFormat monthformat = new SimpleDateFormat("MM", Locale.getDefault());
    SimpleDateFormat monthofdayformat = new SimpleDateFormat("dd", Locale.getDefault());

    final Calendar calendar = Calendar.getInstance(); // 오늘날짜
    final String day = monthofdayformat.format(calendar.getTime());
    String month=monthformat.format(calendar.getTime());

    private Set<HealthPermissionManager.PermissionKey> mKeySet;
    public static final String APP_TAG = "Dangkerbell";
    private HealthDataStore mStore;

    SamsungStepCountReporter sReporter;
   // BloodReporter mReporter;
    glucoseReporter gluecoseReporter;
    SamsungheightReporter hReporter;
    SamsungweightReporter wReporter;


    //구글 로그인
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    StepCountCrud step=StepCountCrud.getInstance();
    static int count=0;
    TextView comment1,comment2,comment3;
    TextView todaystep;
    TextView userid;
    Button drawer_pill,drawer_meal,drawer_blood;
    static ArrayList<String> blood = new ArrayList<>(); //그래프 그리기를 위한 static bgl list
    ArrayList<String> tmp = new ArrayList<>(); //blood 의 불변성을 유지시키기 위한 임시 list
    static String yesterday;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(count==0) {
            mKeySet = new HashSet<PermissionKey>();
            mKeySet.add(new PermissionKey(HealthConstants.Weight.HEALTH_DATA_TYPE, PermissionType.READ));
            mKeySet.add(new PermissionKey(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, PermissionType.READ));
            mKeySet.add(new PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, PermissionType.READ));
            mKeySet.add(new PermissionKey(HealthConstants.Height.HEALTH_DATA_TYPE, PermissionType.READ));

            mStore = new HealthDataStore(this, mConnectionListener);
            mStore.connectService();
            count++;
        }
        mbloodsugar.morningread(month,day);
        mbloodsugar.wakeupread(month,day);
        mbloodsugar.lunchread(month,day);
        mbloodsugar.dinnerread(month,day);
        mbloodsugar.sleepread(month,day);
        mbloodsugar.readyestdayw();
        mbloodsugar.readyestdaym();
        mbloodsugar.readyestdayl();
        mbloodsugar.readyestdayd();
        mbloodsugar.readyestdays();
        mprofile.read();
        setContentView(R.layout.activity_home);

        userid=findViewById(R.id.userid); // !!!!!!!
        mbloodsugar.mHandler1 = new Handler(){
            @Override public void handleMessage(Message msg){
                if (msg.what==1000){
                    tmp.clear();
                    Log.d("Handler","메세지 받음");
                    String mL=mbloodsugar.getMbloodsugar();
                    tmp.add(mbloodsugar.getBloodsugar()); //기상후
                    tmp.add(mbloodsugar.getMbloodsugar()); //아침
                    tmp.add(mbloodsugar.getlbloodsugar()); //점심
                    tmp.add(mbloodsugar.getdbloodsugar()); //저녁
                    tmp.add(mbloodsugar.getsbloodsugar()); //저녁
                    Log.d("메시지 수신 후 기상 후 혈당",mbloodsugar.getMbloodsugar());
                    boolean missing = false; // null값이 입력이 되지 못한 값인지, 미래의 측정치인지를 구별
                    for(int i = tmp.size()-1 ; i > 0; i--){
                        if(tmp.get(i).isEmpty() && missing == false){
                            tmp.remove(i);
                        }
                        else missing = true;
                    }
//                    List<String> lamda = tmp.stream().filter(str->{
//                        if (str.isEmpty() && missing = false) {
//                            return false;
//                        }
//                        missing = true;
//                        return true;
//                    }).collect(Collectors.toList());
                    blood.clear();
                    blood.addAll(tmp);
                    Log.d("blood의 길이:", String.valueOf(blood.size()));
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new LineFragment()).commitAllowingStateLoss();
                }
                if(msg.what==1001){
                    Log.d("Handler2","메세지 받음");
                    Log.d("어제 취침 혈당",mbloodsugar.getYwbloodsugar());
                    Log.d("어제 아침 혈당",mbloodsugar.getYmbloodsugar());
                    Log.d("어제 점심 혈당",mbloodsugar.getYlbloodsuagar());
                    Log.d("어제 저녁 혈당",mbloodsugar.getYdbloodsugar());
                    Log.d("어제 취침 혈당",mbloodsugar.getYsbloodsugar());
                }
                ArrayList<String> yes = new ArrayList<>();
                yes.add(mbloodsugar.getYwbloodsugar());
                yes.add(mbloodsugar.getYmbloodsugar());
                yes.add(mbloodsugar.getYlbloodsuagar());
                yes.add(mbloodsugar.getYdbloodsugar());
                yes.add(mbloodsugar.getYsbloodsugar());
                for(int i = yes.size()-1 ; i >= 0; i--){
                    Log.d("어제 i번째 혈당",i+" : "+yes.get(i));
                    if(!yes.get(i).isEmpty()){
                        yesterday = yes.get(i);
                        Log.d("어제 마지막 혈당",yesterday);
                        break;
                    }
                }
            }
        };






        today=findViewById(R.id.today);
        comment1=findViewById(R.id.comment1);
        comment2=findViewById(R.id.comment2);
        drawer_blood=findViewById(R.id.drawer_blood);
        drawer_meal=findViewById(R.id.drawer_meal);
        drawer_pill=findViewById(R.id.drawer_pill);
        drawer_pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pill = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pill);//혈당관리 클래스 전환
            }
        });
        drawer_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(blood);//혈당관리 클래스 전환
            }
        });
        drawer_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meal = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(meal);//식단관리 클래스 전환
            }
        });

        //  comment3=findViewById(R.id.comment3);
        final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd", Locale.getDefault());

        final Calendar calendar = Calendar.getInstance(); // 오늘날짜

        date = sdf.format(calendar.getTime());
        today.setText(date);


        //bmi<18.5 저체중     bmi>35 과체중
        mprofile.pHandler = new Handler(){
            @Override public void handleMessage(Message msg){

                if (msg.what==1007){
                    //Double bmi=;
                    if(mprofile.getMybmi().equals("")){ //bmi없으면
                        comment1.setText("매일 일정한 시간에 규칙적으로 식사해야 해요. 설탕,꿀 등 단순당 섭취를 주의해주세요.지방을 적당량 섭취하고, 콜레스테롤 섭취를 제한해주세요. ");
                        comment2.setText("내 정보를 입력하시면 더 정확한 건강정보를 볼 수 있습니다!");
                    }
                    else {
                        if (Double.parseDouble(mprofile.getMybmi()) > 35) {
                            comment1.setText("인슐린 저항성은 체지방이 증가할수록 높아집니다. 운동 습관을 점검하고, 운동을 통해 체지방률을 낮추어야 합니다. 탄수화물 과다섭취는 금물이에요 !");
                            comment2.setText("");

                        } else if (mprofile.getMydiabeteskind().equals("1형")) {
                            comment1.setText("당이용 장애로 인해 오히려 혈당이 상승하여 케톤혈증을 일으킬 가능성이 있으므로 적극적인 운동은 오히려 삼가하도록 하는 것이 좋습니다. 운동 시 케톤혈증이 자주 일어난다면 인슐린 투여량을 줄이는 것 보다는 간식으로 조정하는 것이 좋습니다. ");
                        } else if (mprofile.getMyhealdiabetes().equals("경구혈당강하제 단독")) {
                            comment1.setText("경구 혈당강하제로 치료하고 있으시므로 약물의 최대작용시간을 피해서 운동하는 것이 저혈당 방지에 도움이 됩니다.");
                            comment2.setVisibility(View.GONE);
                        } else {
                            comment1.setText("매일 일정한 시간에 규칙적으로 식사해야 해요.");
                            comment2.setText("소금 섭취를 줄이고, 단순당 섭취에 주의해주세요.");
                        }
                    }
                }
            }
        };

        Log.d("우저아이디",User);
        userid.setText(User);

       todaystep=findViewById(R.id.todaystep);





       step.read();
    //   todaystep.setText(sReporter.count); //오류


            step.stepHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1007) {
                        todaystep.setText(step.getstep());
                    }
                }

            };

        toolbar=findViewById(R.id.toolbar_menu);
        drawerLayout=findViewById(R.id.drawer_layout) ;
        mypage = findViewById(R.id.mypage);
        logout = findViewById(R.id.logout);
        drawerView=findViewById(R.id.drawer);
        settime=findViewById(R.id.settime);
        close=findViewById(R.id.toolbar_close);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent drawer = new Intent(getApplicationContext(), DrawerActivity.class);
//                startActivity(drawer);//액티비티 띄우기
                drawerLayout.openDrawer(drawerView);


            }
        });
        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setTimeintent = new Intent(getApplicationContext(), mysetTimeActivity.class);
                startActivity(setTimeintent);//액티비티 띄우기
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
            }
        });
        mypage.setOnClickListener(new View.OnClickListener() { // 내 정보 버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), myprofileActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        logout.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭

            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"로그아웃 클릭");
                signOut();
            }
        });
        profile=findViewById(R.id.myprofile); // 내 정보 버튼
        profile.setOnClickListener(new View.OnClickListener() { // 내 정보 버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), myprofileActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
        blood_btn=findViewById(R.id.blood_btn);
        meal_btn=findViewById(R.id.meal_btn);
        pill_btn=findViewById(R.id.pill_btn);
        blood_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(blood);//혈당관리 클래스 전환
            }
        });
        meal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meal = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(meal);//식단관리 클래스 전환
            }
        });
        pill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pill = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pill);//복약관리 클래스 전환
            }
        });

    }
    @Override
    public void onDestroy() {
        try{
            mStore.disconnectService();
        }catch(Exception e){
            Log.e("class",e.toString());
        }
        super.onDestroy();
    }
    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            wReporter=new SamsungweightReporter(mStore);

            hReporter=new SamsungheightReporter(mStore);

            sReporter = new SamsungStepCountReporter(mStore);
            // mReporter = new BloodReporter(mStore);
            gluecoseReporter=new glucoseReporter(mStore);

            if (isPermissionAcquired()) {
                wReporter.start(wObserver); //몸무게

                hReporter.start(hObserver); //키

                sReporter.start(stepCountObserver); //걸음수
                // mReporter.start(bObserver);
                gluecoseReporter.start(gObserver); //혈당
            } else {
                requestPermission();
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult error) {
            Log.d(APP_TAG, "Health data service is not available.");
            showConnectionFailureDialog(error);
        }

        @Override
        public void onDisconnected() {
            Log.d(APP_TAG, "Health data service is disconnected.");
            if (!isFinishing()) {
                mStore.connectService();
            }
        }
    };

    private void showPermissionAlarmDialog() { //?
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(homeActivity.this);
        alert.setTitle(R.string.notice)
                .setMessage(R.string.msg_perm_acquired)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (error.hasResolution()) {
            switch (error.getErrorCode()) {
                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                    alert.setMessage(R.string.msg_req_install);
                    break;
                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                    alert.setMessage(R.string.msg_req_upgrade);
                    break;
                case HealthConnectionErrorResult.PLATFORM_DISABLED:
                    alert.setMessage(R.string.msg_req_enable);
                    break;
                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                    alert.setMessage(R.string.msg_req_agree);
                    break;
                default:
                    alert.setMessage(R.string.msg_req_available);
                    break;
            }
        } else {
            alert.setMessage(R.string.msg_conn_not_available);
        }

        alert.setPositiveButton(R.string.ok, (dialog, id) -> {
            if (error.hasResolution()) {
                error.resolve(homeActivity.this);
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton(R.string.cancel, null);
        }

        alert.show();
    }

    private boolean isPermissionAcquired() {

        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        try {
            // Check whether the permissions that this application needs are acquired
            Map<PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(mKeySet);
            //Log.d("혈당2",mReporter.count);

            return resultMap.get(mKeySet);

        } catch (Exception e) {
            Log.e(APP_TAG, "Permission request fails.", e);
        }
        return false;
    }

    private void requestPermission() {
//        mKeySet.add(new PermissionKey(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, PermissionType.READ));
//        mKeySet.add(new PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE,PermissionType.READ));
        //PermissionKey permKey = new PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE, PermissionType.READ);
        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
        // mStepCountTv.setText(mReporter.count);

        try {

            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(mKeySet, homeActivity.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {

                            updateStepCountView("");
                            showPermissionAlarmDialog();
                        } else {
                            wReporter.start(wObserver); //몸무게

                            //    mStepCountTv.setText(mReporter.count);

                            sReporter.start(stepCountObserver); //걸음수
                            // mReporter.start(bObserver);
                            gluecoseReporter.start(gObserver); //혈당
                            hReporter.start(hObserver); //키


                        }
                    });
        } catch (Exception e) {
            Log.e(APP_TAG, "Permission setting fails.", e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private SamsungStepCountReporter.StepCountObserver stepCountObserver = new SamsungStepCountReporter.StepCountObserver() {
        @Override
        public void onChanged(int count) {
            homeActivity.this.updateStepCountView(String.valueOf(count));
        }
    };
    private SamsungheightReporter.HeightObserver hObserver=new SamsungheightReporter.HeightObserver() {
        @Override
        public void onChanged(String count) {
            homeActivity.this.updateStepCountView(String.valueOf(count));

        }
    };
    //    private BloodReporter.BloodObserver bObserver = new BloodReporter.BloodObserver() {
//
//
//
//        @Override
//
//        public void onChanged(String count) {
//
//            Log.d(APP_TAG, "Step reported : " + count);
//
//            homeActivity.this.updateStepCountView(String.valueOf(count));
//
//        }
//
//    };
    private glucoseReporter.BloodglucoseObserver gObserver = new glucoseReporter.BloodglucoseObserver() {
        @Override
        public void onChanged(String count) {
            Log.d(APP_TAG, "Step reported : " + count);
            homeActivity.this.updateStepCountView(String.valueOf(count));
        }
    };

    private SamsungweightReporter.WeightObserver wObserver=new SamsungweightReporter.WeightObserver() {
        @Override
        public void onChanged(String count) {
            Log.d(APP_TAG, "Step reported : " + count);
            homeActivity.this.updateStepCountView(String.valueOf(count));

        }
    };
    //
    private void updateStepCountView(final String count) {
        todaystep.setText(count);

    }
    //    @Override
//    public boolean onOptionsItemSelected(android.view.MenuItem item) {
//
//        if (item.getItemId() == R.id.connect) {
//            requestPermission();
//        }
//
//        return true;
//    }
    public void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent loginintent = new Intent(getApplicationContext(), LoginInActivity.class);
                        startActivity(loginintent);//액티비티 띄우기 새로 추가 - 로그인 전환
                        finishAffinity();
                    }
                });
    }
}
