package edu.odu.ece486.hm_app;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.UUID;

public class cBaseApplication extends Application implements BluetoothAdapter.LeScanCallback {

    public RFduinoService rfduinoService;

    // State machine
    final private static int STATE_BLUETOOTH_OFF = 1;
    final private static int STATE_DISCONNECTED = 2;
    final private static int STATE_CONNECTING = 3;
    final private static int STATE_CONNECTED = 4;

    private int state;

    private boolean scanStarted;
    private boolean scanning;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private String bluetoothDeviceInfo;
    private String expectedBluetoothDeviceName = "Heart";

    private final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            if (state == BluetoothAdapter.STATE_ON) {
                upgradeState(STATE_DISCONNECTED);
            } else if (state == BluetoothAdapter.STATE_OFF) {
                downgradeState(STATE_BLUETOOTH_OFF);
            }
        }
    };

    private final BroadcastReceiver scanModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scanning = (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_NONE);
            scanStarted &= scanning;
            StatusUpdate();
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();

        Log.d("cBaseApplication", "Creating cBaseApplication");

        //Enable bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String enableStatus = bluetoothAdapter.enable() ? "Enabling bluetooth..." : "Enable failed!";
        Log.d("cBaseApplication", enableStatus);

        registerReceiver(scanModeReceiver, new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
        registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(rfduinoReceiver, RFduinoService.getIntentFilter());

        updateState(bluetoothAdapter.isEnabled() ? STATE_DISCONNECTED : STATE_BLUETOOTH_OFF);

        //Find Heart Device
        Log.d("cBaseApplication", "Begin LE Scan.");
        scanStarted = true;
        bluetoothAdapter.startLeScan(
                new UUID[]{RFduinoService.UUID_SERVICE}, cBaseApplication.this);
    }

    private final ServiceConnection rfduinoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            rfduinoService = ((RFduinoService.LocalBinder) service).getService();
            if (rfduinoService.initialize()) {
                if (rfduinoService.connect(bluetoothDevice.getAddress())) {
                    upgradeState(STATE_CONNECTING);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            rfduinoService = null;
            downgradeState(STATE_DISCONNECTED);
        }
    };

    private final BroadcastReceiver rfduinoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (RFduinoService.ACTION_CONNECTED.equals(action)) {
                upgradeState(STATE_CONNECTED);
            } else if (RFduinoService.ACTION_DISCONNECTED.equals(action)) {
                downgradeState(STATE_DISCONNECTED);
            } else if (RFduinoService.ACTION_DATA_AVAILABLE.equals(action)) {
                decipherHeartData(intent.getByteArrayExtra(RFduinoService.EXTRA_DATA));
                //addData(intent.getByteArrayExtra(RFduinoService.EXTRA_DATA));

            }
        }
    };

    private void decipherHeartData(byte[] byteArrayExtra) {
        if((char)byteArrayExtra[0] == 'p')
        {
            Log.d("Pressure", "New pressure = " + (int)byteArrayExtra[1]);
            int pressure = (int)byteArrayExtra[1];
            ValsalvaDataHolder.getInstance().updatePressure(pressure);
        }
    }

    @Override
    public void onTerminate() {
        Log.d("cBaseApplication", "Terminating cBaseApplication...");
        super.onTerminate();
        bluetoothAdapter.stopLeScan(this);

        unregisterReceiver(scanModeReceiver);
        unregisterReceiver(bluetoothStateReceiver);
        unregisterReceiver(rfduinoReceiver);

        Log.d("cBaseApplication", "cBaseApplication terminated.");
    }

    private void upgradeState(int newState) {
        if (newState > state) {
            updateState(newState);
        }
    }

    private void downgradeState(int newState) {
        if (newState < state) {
            updateState(newState);
        }
    }

    private void updateState(int newState) {
        state = newState;
        StatusUpdate();
    }

    private void StatusUpdate() {
        // Bluetooth Status
        boolean on = state > STATE_BLUETOOTH_OFF;
        Log.d("Bluetooth Status", on ? "Bluetooth enabled." : "Bluetooth disabled.");

        // Scan Status
        if (scanStarted && scanning) {
            Log.d("Scan Status", "Scanning...");
        } else if (scanStarted) {
            Log.d("Scan Status", "Scan started.");
        } else {
            Log.d("Scan Status", "Scan not active.");
        }

        // Connection Status
        boolean connected = false;
        String connectionText = "Disconnected";
        if (state == STATE_CONNECTING) {
            connectionText = "Connecting...";
        } else if (state == STATE_CONNECTED) {
            connected = true;
            connectionText = "Connected";
        }

        Log.d("Connection Status", connectionText);

        Log.d("State", "state = " + state);
    }

    @Override
    public void onLeScan(BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        Log.d("cBaseApplication", "LE Scan Data Received.");
        bluetoothAdapter.stopLeScan(this);
        bluetoothDevice = device;
        bluetoothDeviceInfo = BluetoothHelper.getDeviceInfoText(bluetoothDevice, rssi, scanRecord);
        Log.d("cBaseApplication", "Verifying BT device is heart.");
        //Verify Device Info
        //TODO: Verify that the scan returned "Heart" bluetooth device prior to connecting.
        //if(bluetoothDevice.getName() == expectedBluetoothDeviceName) {
            //Connect to device.
            Log.d("cBaseApplication", "Connecting to BT Smart Device...");
            Intent rfduinoIntent = new Intent(cBaseApplication.this, RFduinoService.class);
            getApplicationContext().bindService(rfduinoIntent, rfduinoServiceConnection, BIND_AUTO_CREATE);
        //}

        StatusUpdate();
    }

    public void sendBeginDataTransferCommand() {
        //Send signal to begin data transfer
        rfduinoService.send(new byte[]{0});
    }
}
