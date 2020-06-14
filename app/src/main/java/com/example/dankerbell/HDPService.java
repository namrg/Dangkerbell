package com.example.dankerbell;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothHealthAppConfiguration;
import android.bluetooth.BluetoothHealthCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class HDPService extends Service {
    // 어플리케이션 등록 완료
    public static final int STATUS_HEALTH_APP_REG = 100;
    // 디바이스에서 데이터를 읽기 시작할 때
    public static final int STATUS_READ_DATA = 101;
    // 디바이스에서 데이터를 읽을 때
    public static final int STATUS_READ_DATA_SYSTOLIC = 102;
    public static final int STATUS_READ_DATA_DIASTOLIC = 103;
    public static final int STATUS_READ_DATA_PULSE = 104;
    // 디바이스에서 데이터 읽기를 완료할 때
    public static final int STATUS_READ_DATA_DONE = 105;
    // HDPActivity에서의 메시지 코드
    public static final int MSG_REG_CLIENT = 200;
    public static final int MSG_REG_HEALTH_APP = 201;

    private BluetoothHealthAppConfiguration mHealthAppConfig;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothHealth mBluetoothHealth;
    private Messenger mClient;

    // 이벤트 처리기
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 클라이언트의 서비스 등록
                case MSG_REG_CLIENT:
                    mClient = msg.replyTo;
                    break;
                // 어플리케이션 등록
                case MSG_REG_HEALTH_APP:
                    registerApp(msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            stopSelf();
            return;
        }
        // 기본 어댑터에서 디바이스에 연결 권한 취득
        if (!mBluetoothAdapter.getProfileProxy(this, mBluetoothServiceListener,
                BluetoothProfile.HEALTH)) {
            Toast.makeText(this, "동작이안되요",
                    Toast.LENGTH_LONG);
            stopSelf();
            return;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    };

    // 어플리케이션 등록
    private void registerApp(int dataType) {
        mBluetoothHealth.registerSinkAppConfiguration(
                "HDPService",
                dataType,
                mHealthCallback);
    }

    // 서비스 리스너
    private final BluetoothProfile.ServiceListener
            mBluetoothServiceListener =
            new BluetoothProfile.ServiceListener() {
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
                    // 헬스 디바이스 연결이 끊어진 경우
                    if (profile == BluetoothProfile.HEALTH) {
                        mBluetoothHealth = (BluetoothHealth) proxy;
                    }
                }

                public void onServiceDisconnected(int profile) {
                    if (profile == BluetoothProfile.HEALTH) {
                        // 헬스 디바이스가 끊어진 경우
                        mBluetoothHealth = null;
                    }
                }
            };

    // 콜백 처리
    private final BluetoothHealthCallback mHealthCallback =
            new BluetoothHealthCallback() {
                public void onHealthAppConfigurationStatusChange(
                        BluetoothHealthAppConfiguration config, int status) {
                    // 어플리케이션 설정정보 알림 상태
                    if (status == BluetoothHealth.APP_CONFIG_REGISTRATION_FAILURE) {
                        // 실패
                        mHealthAppConfig = null;
                        sendMessage(STATUS_HEALTH_APP_REG, -1);
                    } else if (status ==
                            BluetoothHealth.APP_CONFIG_REGISTRATION_SUCCESS) {
                        // 성공
                        mHealthAppConfig = config;
                        sendMessage(STATUS_HEALTH_APP_REG, 0);
                    }
                }

                // 연결 정보 변경 시
                public void onHealthChannelStateChange(
                        BluetoothHealthAppConfiguration config,
                        BluetoothDevice device, int prevState, int newState,
                        ParcelFileDescriptor fd, int channelId) {
                    if (prevState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED &&
                            newState == BluetoothHealth.STATE_CHANNEL_CONNECTED) {
                        if (config.equals(mHealthAppConfig)) {
                            (new ReadThread(fd)).start();
                        }
                    }
                }
            };

    // HDPActivity에 메시지 송신 처리
    private void sendMessage(int what, int value) {
        if (mClient == null) {
            return;
        }
        try {
            mClient.send(Message.obtain(null, what, value, 0));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 디바이스에서 데이터를 수신하는 스레드
    private class ReadThread extends Thread {
        private ParcelFileDescriptor mFd;

        public ReadThread(ParcelFileDescriptor fd) {
            super();
            mFd = fd;
        }

        @Override
        public void run() {
            FileInputStream fis =
                    new FileInputStream(mFd.getFileDescriptor());
            FileOutputStream fos =
                    new FileOutputStream(mFd.getFileDescriptor());
            final byte data[] = new byte[512];
            try {
                while(fis.read(data) > -1) {
                    sendMessage(STATUS_READ_DATA, 0);
                    // 이하 OMRON 혈압계 HEM-7081-IT용 하드 코딩이다
                    // 기기 고유정보도 설명되어 있으므로 이용기기에 맞추어 변경이 필요하다
                    // 자세한 내용은 ISO/IEEE 11073-10407의 사양서를 참조한다
                    if (data[0] == -30) {
                        Log.i("HealthDevice", "E2.2.2 Association request");
                        byte[] send = { (byte) 0xE3, 0x00, 0x00, 0x2C, 0x00,
                                0x03, 0x50, 0x79, 0x00, 0x26,
                                (byte) 0x80, 0x00, 0x00, 0x00, (byte) 0x80,
                                0x00, (byte) 0x80, 0x00, 0x00, 0x00,
                                0x00, 0x00, 0x00, 0x00, (byte) 0x80,
                                0x00, 0x00, 0x00, 0x00, 0x08,
                                0x00, 0x22, 0x09, 0x22, 0x58, 0x08,
                                0x03, (byte) 0xB3, 0x00, 0x00, 0x00,
                                0x00, 0x00, 0x00, 0x00, 0x00,
                                0x00, 0x00};
                        fos.write(send);
                        Log.i("HealthDevice",
                                "E2.2.3 Association response");
                    }
                    if (data[0] == -25) {
                        if(data[7] == 1) {
                            Log.i("HealthDevice",
                                    "E3.2.2 Remote operation invoke event report configuration");
                            byte[] send = {(byte) 0xE7, 0x00, 0x00, 0x16, 0x00,
                                    0x14, 0x00, 0x01, 0x02, 0x01,
                                    0x00, 0x0E, 0x00, 0x00, 0x00,
                                    0x00, 0x00, 0x00, 0x0D, 0x1C,
                                    0x00, 0x04, 0x40, 0x00, 0x00, 0x00};
                            fos.write(send);
                            Log.i("HealthDevice",
                                    "E3.2.3 Remote operation response event report configuration");
                            sleep(500);
                            // Send "Get all medical device system attributes request"
                            byte[] send2 = {(byte) 0xE7, 0x00, 0x00, 0x0E, 0x00,
                                    0x0C, 0x00, 0x01, 0x01, 0x03,
                                    0x00, 0x06, 0x00, 0x00, 0x00,
                                    0x00, 0x00, 0x00};
                            fos.write(send2);
                            Log.i("HealthDevice",
                                    "E4.1.2 Get all medical device system attributes request");
                        }
                        if(data[7] == 2) {
                            Log.i("HealthDevice",
                                    "E.5.1 Confirmed measurement data transmission");
                            sendMessage(STATUS_READ_DATA_SYSTOLIC, data[45]& 0xFF);
                            sendMessage(STATUS_READ_DATA_DIASTOLIC, data[47]& 0xFF);
                            sendMessage(STATUS_READ_DATA_PULSE, data[63]& 0xFF);
                        }
                    }
                    if (data[0] == -28) {
                        // Receive "Association release request"
                        Log.i("HealthDevice", "E6.1 Association release request");
                        // Send "Association release response"
                        byte[] send = {(byte) 0xE5, 0x00, 0x00, 0x02, 0x00, 0x00};
                        fos.write(send);
                        Log.i("HealthDevice", "E6.2 Association release response");
                    }
                    sleep(1000);
                }
            } catch(IOException ioe) {} catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mFd != null) {
                try {
                    mFd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sendMessage(STATUS_READ_DATA_DONE, 0);
        }
    }
}