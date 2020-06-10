package com.example.dankerbell;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dankerbell.Bluetooth.ConnectBluetoothActivity;
import com.example.dankerbell.Firebase.StepCountCrud;
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

public class homeActivity extends AppCompatActivity { // 홈화면 클래스

    FragmentPagerAdapter adapterViewPager;
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        //private static int NUM_ITEMS = 2;
        static int NUM_ITEMS;
        public MyPagerAdapter(FragmentManager fragmentManager,int NUM_ITEMS) {
                super(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                this.NUM_ITEMS=NUM_ITEMS;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            Log.d("실행o",String.valueOf(position));

            switch (position) {

                case 0:

                    return FirstFragment.newInstance(0, "Page # 1");
                case 1:
                    return SecondFragment.newInstance(1, "Page # 2");
                //      case 2:
                //  return ThirdFragment.newInstance(2, "Page # 3");
                default:
                    return null;

            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    TextView profile;
    Button blood_btn,meal_btn,pill_btn;
    TextView bluetooth;
    TextView toolbar;
    TextView userid;
    Button drawer_pill,drawer_blood,drawer_meal;
    TextView close;
    TextView todaystep;
    DrawerLayout drawerLayout;
    View drawerView;
    Button logout,settime;
    Button mypage;

    private Set<HealthPermissionManager.PermissionKey> mKeySet;
    int m=0;
    public static final String APP_TAG = "Dangkerbell";
    private HealthDataStore mStore;

    SamsungStepCountReporter sReporter;
    BloodReporter mReporter;
    glucoseReporter gluecoseReporter;
    SamsungheightReporter hReporter;
    SamsungweightReporter wReporter;
    @Override
    public void onDestroy() {
        mStore.disconnectService();
        super.onDestroy();
    }

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {

        @Override
        public void onConnected() {
            Log.d(APP_TAG, "Health data service is connected.");
            sReporter = new SamsungStepCountReporter(mStore);
            mReporter = new BloodReporter(mStore);
            gluecoseReporter=new glucoseReporter(mStore);
            wReporter=new SamsungweightReporter(mStore);

            hReporter=new SamsungheightReporter(mStore);

            if (isPermissionAcquired()) {
                //   mReporter.start(mStepCountObserver);
                sReporter.start(stepCountObserver);
                mReporter.start(bObserver);
                gluecoseReporter.start(gObserver);
                wReporter.start(wObserver);

                hReporter.start(hObserver);
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

    private void showPermissionAlarmDialog() {
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

        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);


        try {

            // Show user permission UI for allowing user to change options
            pmsManager.requestPermissions(mKeySet, homeActivity.this)
                    .setResultListener(result -> {
                        Log.d(APP_TAG, "Permission callback is received.");
                        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

                        if (resultMap.containsValue(Boolean.FALSE)) {
                            //Log.d("혈당5",mReporter.count);

                            updateStepCountView("");
                            showPermissionAlarmDialog();
                        } else {
//                            Log.d("혈압6",mReporter.count);
//                            Log.d("혈당",gluecoseReporter.count);
                            //    mStepCountTv.setText(mReporter.count);
                            wReporter.start(wObserver);

                            sReporter.start(stepCountObserver);
                            mReporter.start(bObserver);
                            gluecoseReporter.start(gObserver);
                            hReporter.start(hObserver);
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
    private BloodReporter.BloodObserver bObserver = new BloodReporter.BloodObserver() {



        @Override

        public void onChanged(String count) {

            Log.d(APP_TAG, "Step reported : " + count);

            homeActivity.this.updateStepCountView(String.valueOf(count));

        }

    };
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

    //구글 로그인
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();
    StepCountCrud step=StepCountCrud.getInstance();
    int count=0;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setAdapter(adapterViewPager);
       todaystep=findViewById(R.id.todaystep);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),2);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vpPager);
      //  adapterViewPager.registerDataSetObserver(indicator.getDataSetObserver());

        // Request the connection to the health data store
        if(count==0)     {
            mKeySet = new HashSet<PermissionKey>();
            mKeySet.add(new PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, PermissionType.READ));
            mKeySet.add(new PermissionKey(HealthConstants.Weight.HEALTH_DATA_TYPE, PermissionType.READ));
            mKeySet.add(new PermissionKey(HealthConstants.Height.HEALTH_DATA_TYPE, PermissionType.READ));
            mKeySet.add(new PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE, PermissionType.READ));
            mKeySet.add(new PermissionKey(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, PermissionType.READ));

            mStore = new HealthDataStore(this, mConnectionListener);
            mStore.connectService();}
        count++;
        Log.d("count",String.valueOf(count));
 


        step.read();

//        todaystep.setText(sReporter.count);


            step.stepHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1007) {
                        todaystep.setText(step.getstep());
                    }
                }

            };

        toolbar=findViewById(R.id.toolbar_menu);
        drawerLayout=findViewById(R.id.drawer_layout);

        drawer_blood=findViewById(R.id.drawer_blood);
        drawer_pill=findViewById(R.id.drawer_pill);
        drawer_meal=findViewById(R.id.drawer_meal);

        mypage = findViewById(R.id.mypage);
        logout = findViewById(R.id.logout);
        drawerView=findViewById(R.id.drawer);
        settime=findViewById(R.id.settime);
        close=findViewById(R.id.toolbar_close);
        userid=findViewById(R.id.userid);
        userid.setText(User);
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
                Intent intent = new Intent(getApplicationContext(), myprofileActivity.class); // 이미 내가 저장한 프로필
                startActivity(intent);//액티비티 띄우기
            }
        });
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
        logout.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭

            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"로그아웃 클릭");
                signOut();          }
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
        bluetooth=findViewById(R.id.bluetooth);
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bluetootintent=new Intent(getApplicationContext(),ConnectBluetoothActivity.class);
                startActivity(bluetootintent);
            }
        });
    }


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
