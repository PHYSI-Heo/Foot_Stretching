package com.physis.foot.stretching;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.physis.foot.stretching.dialog.MyAlertDialog;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.widget.MenuButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends HttpAsyncTaskActivity implements View.OnClickListener {

    private MyAlertDialog registerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        if(url.equals(HttpPacket.REGISTER_USER_URL)){
            registerDialog.dismiss();
            Toast.makeText(getApplicationContext(), "사용자 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mbtn_user_register:
                showUserRegisterDialog();
                break;
            case R.id.mbtn_user_scheduler:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
            case R.id.mbtn_simple_user:
                startActivity(new Intent(this, SimpleUserActivity.class));
                break;
            case R.id.iv_btn_set_pattern:
                startActivity(new Intent(this, ManagerActivity.class));
                break;
        }
    }


    private void registerUser(String name, String phone){
        if(name == null || name.length() == 0 || phone == null || phone.length() == 0){
            Toast.makeText(getApplicationContext(), "사용자 이름 / 전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_USER_NAME, name);
            params.put(HttpPacket.PARAMS_USER_PHONE, phone);
            requestAPI(HttpPacket.REGISTER_USER_URL, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showUserRegisterDialog(){
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_user_register, null);

        final EditText etName = view.findViewById(R.id.et_user_name);
        final EditText etPhone = view.findViewById(R.id.et_phone_num);

        registerDialog = new MyAlertDialog();
        registerDialog.show(MainActivity.this, null, view,
                "등록", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerUser(etName.getText().toString(), etPhone.getText().toString());
                    }
                });
    }

    private void init() {
        AnimationDrawable animDrawable = (AnimationDrawable)findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();

        MenuButton mbtnUserRegister = findViewById(R.id.mbtn_user_register);
        mbtnUserRegister.setOnClickListener(this);
        MenuButton mbtnUserSchedule = findViewById(R.id.mbtn_user_scheduler);
        mbtnUserSchedule.setOnClickListener(this);
        MenuButton mbtnSimpleUser = findViewById(R.id.mbtn_simple_user);
        mbtnSimpleUser.setOnClickListener(this);

        ImageView iBtnPatternSetup = findViewById(R.id.iv_btn_set_pattern);
        iBtnPatternSetup.setOnClickListener(this);
    }
}