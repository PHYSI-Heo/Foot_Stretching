package com.physis.foot.stretching;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.physis.foot.stretching.ble.BluetoothLEManager;
import com.physis.foot.stretching.data.HospitalInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.dialog.MyAlertDialog;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.widget.MenuButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends HttpAsyncTaskActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BluetoothLEManager bleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bleManager = BluetoothLEManager.getInstance(getApplicationContext());
        bleManager.bindService();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleManager.unBindService();
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mbtn_user_manager:
                startActivity(new Intent(this, UserSetupActivity.class));
//                showUserRegisterDialog();
                break;
            case R.id.mbtn_user_scheduler:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
            case R.id.mbtn_simple_user:
                startActivity(new Intent(this, SimpleUserActivity.class));
                break;
            case R.id.mbtn_pattern_setting:
                startActivity(new Intent(this, ManagerActivity.class));
                break;
        }
    }

    private void init() {
        AnimationDrawable animDrawable = (AnimationDrawable)findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();

        MenuButton mbtnUserRegister = findViewById(R.id.mbtn_user_manager);
        mbtnUserRegister.setOnClickListener(this);
        MenuButton mbtnUserSchedule = findViewById(R.id.mbtn_user_scheduler);
        mbtnUserSchedule.setOnClickListener(this);
        MenuButton mbtnSimpleUser = findViewById(R.id.mbtn_simple_user);
        mbtnSimpleUser.setOnClickListener(this);

        MenuButton mbtnSetMyPattern = findViewById(R.id.mbtn_pattern_setting);
        mbtnSetMyPattern.setOnClickListener(this);

        TextView tvHospitalName = findViewById(R.id.tv_hospital_name);
        tvHospitalName.setText(HospitalInfo.getInstance().getName());
    }
}