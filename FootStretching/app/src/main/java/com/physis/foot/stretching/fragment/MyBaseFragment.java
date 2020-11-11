package com.physis.foot.stretching.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.physis.foot.stretching.ble.BluetoothLEManager;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.http.HttpAsyncTask;
import com.physis.foot.stretching.http.HttpPacket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class MyBaseFragment extends Fragment {

    public static final String HM_10_CONF = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String HM_RX_TX = "0000ffe1-0000-1000-8000-00805f9b34fb";

    public static final String DEVICE_ADDRESS = "A8:1B:6A:9F:64:C0";

    protected BluetoothLEManager bleManager = BluetoothLEManager.getInstance(getActivity());
    private BluetoothDevice targetDevice = null;

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
            switch (msg.what) {
                case BluetoothLEManager.BLE_SCAN_START:
                    break;
                case BluetoothLEManager.BLE_SCAN_STOP:
                    if(targetDevice == null){
                        LoadingDialog.dismiss();
                        Toast.makeText(getActivity(), "족부운동기구의 전원을 확인하세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        bleManager.connect(targetDevice);
                    }
                    break;
                case BluetoothLEManager.BLE_SCAN_DEVICE:
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    if(device.getAddress().equals(DEVICE_ADDRESS))
                        targetDevice = device;
                    break;
                case BluetoothLEManager.BLE_CONNECT_DEVICE:
                    break;
                case BluetoothLEManager.BLE_SERVICES_DISCOVERED:
                    if (!bleManager.notifyCharacteristic(HM_10_CONF, HM_RX_TX)) {
                        bleManager.disconnect();
                    }
                    break;
                case BluetoothLEManager.BLE_READ_CHARACTERISTIC:
                    onReadyToDevice();
                    break;
                case BluetoothLEManager.BLE_DATA_AVAILABLE:
                    onReceiveAck((String)msg.obj);
                    break;
                case BluetoothLEManager.BLE_DISCONNECT_DEVICE:
                    LoadingDialog.dismiss();
                    Toast.makeText(getActivity(), "족부운동기구와 연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    protected void sendControlMessage(String msg){
        bleManager.writeCharacteristic("$" + msg + "#");
    }

    protected void onReceiveAck(String msg){

    }

    protected void onReadyToDevice(){
        LoadingDialog.dismiss();
        Toast.makeText(getActivity(), "족부 패턴운동을 시작합니다.", Toast.LENGTH_SHORT).show();
    }

    protected void connectBoard(){
        if(bleManager.isConnected()){
            onReadyToDevice();
        }else{
            targetDevice = null;
            bleManager.scan(true, true);
        }
    }

}
