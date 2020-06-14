package com.example.dankerbell;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dankerbell.HDPService;
import com.example.dankerbell.R;

public class HDPActivity extends Activity {
    // 연결하는 건강 기기가 사용할 적절한 IEEE11073 데이터 형식을 지정한다
    // 예)
    // 0×1006 - 심전계 IEEE11073-10406
    // 0×1007 - 혈압계 IEEE11073-10407
    // 0×1008 - 체온계 IEEE11073-10408
    // 0×100F - 체중계 IEEE11073-10415
    private static final int HEALTH_PROFILE_SOURCE_DATA_TYPE = 0x1007;

    private TextView mStatusMessage;
    private TextView mSystolicData;
    private TextView mDiastolicData;
    private TextView mPulseData;

    private BluetoothAdapter mBluetoothAdapter;
    private Resources mRes;
    private Messenger mHealthService;
    private boolean mHealthServiceBound;

    // BluetoothHDPService에서의 이벤트 처리기
    private Handler mIncomingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HDPService.STATUS_HEALTH_APP_REG:
                    String status;
                    // 어플리케이션 등록이 완료되었을 때
                    if(msg.arg1 == 0) {
                        status = "Available";
                    } else {
                        status = "Not Available";
                    }
//                    mStatusMessage.setText(
//                            String.format(mRes.getString(R.string.status_reg), status));
//                    mStatusMessage.setText(
//                            String.format("상태", status));
                    mStatusMessage.setText(status);
                    break;
                case HDPService.STATUS_READ_DATA:
                    // 디바이스에서의 데이터를 읽고 있을 때
                  //  mStatusMessage.setText(mRes.getString(R.string.read_data));
                    mStatusMessage.setText("데이터 읽고 있음 !");
                    // 텍스트 뷰의 초기화
                    mSystolicData.setText(null);
                    mDiastolicData.setText(null);
                    mPulseData.setText(null);
                    break;
                case HDPService.STATUS_READ_DATA_SYSTOLIC:
                    // 혈압(수축기) 데이터
                    mSystolicData.setText("Blood Pressure(Systole):" + msg.arg1);
                    break;
                case HDPService.STATUS_READ_DATA_DIASTOLIC:
                    // 혈압(확장기) 데이터
                    mDiastolicData.setText("Blood Pressure(Distole):" + msg.arg1);
                    break;
                case HDPService.STATUS_READ_DATA_PULSE:
                    // 맥박 데이터
                    mPulseData.setText("Pulse:" + msg.arg1);
                    break;
                case HDPService.STATUS_READ_DATA_DONE:
                    // 디바이스에서의 데이터 읽기가 끝났을 때
                    //mStatusMessage.setText(mRes.getString(R.string.read_data_done));
                    mStatusMessage.setText("데이터 읽기 끝 !");

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private final Messenger mMessenger = new Messenger(mIncomingHandler);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 단말기 Bluetooth 통신이 사용 가능한지 점검한다
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(
                    this, "블루투스 연결 X", Toast.LENGTH_LONG);
            finish();
            return;
        }
        setContentView(R.layout.activity_bloodbluetooth);
        mStatusMessage = (TextView) findViewById(R.id.status_msg);
        mSystolicData = (TextView) findViewById(R.id.systolic);
        mDiastolicData = (TextView) findViewById(R.id.diastolic);
       mPulseData = (TextView) findViewById(R.id.pulse);
        mRes = getResources();
        mHealthServiceBound = false;

        registerReceiver(mReceiver, initIntentFilter());

        // 초기화 처리
        if (mBluetoothAdapter.isEnabled()) {
            initialize();
        }
    }

    // HDPServic와의 통신 셋업
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mHealthServiceBound = true;
            Message msg = Message.obtain(null, HDPService.MSG_REG_CLIENT);
            msg.replyTo = mMessenger;
            mHealthService = new Messenger(service);
            try {
                mHealthService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            sendMessage(HDPService.MSG_REG_HEALTH_APP,
                    HEALTH_PROFILE_SOURCE_DATA_TYPE);
        }
        // 서비스가 끊어졌을 때
        public void onServiceDisconnected(ComponentName name) {
            mHealthService = null;
            mHealthServiceBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHealthServiceBound) unbindService(mConnection);
        unregisterReceiver(mReceiver);
    }

    private void initialize() {
        // HDPService 시작
        Intent intent = new Intent(this, HDPService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    // 인텐트 필터
    private IntentFilter initIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return filter;
    }

    // 브로드캐스트 수신기
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR) == BluetoothAdapter.STATE_ON) {
                    initialize();
                }
            }
        }
    };

    // HDPService에 Message 송신
    private void sendMessage(int what, int value) {
        if (mHealthService == null) {
            return;
        }

        try {
            mHealthService.send(Message.obtain(null, what, value, 0));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}