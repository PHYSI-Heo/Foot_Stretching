package com.physis.foot.stretching;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.physis.foot.stretching.dialog.MyAlertDialog;
import com.physis.foot.stretching.widget.MenuButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MenuButton mbtnUserRegister, mbtnUserSchedule, mbtnSimpleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
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
                break;
        }
    }


    private void showUserRegisterDialog(){
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_user_register, null);

        final EditText etName = view.findViewById(R.id.et_user_name);
        final EditText etPhone = view.findViewById(R.id.et_phone_num);

        MyAlertDialog dialog = new MyAlertDialog();
        dialog.show(MainActivity.this, null, view,
                "등록", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
    }

    private void init() {
        AnimationDrawable animDrawable = (AnimationDrawable)findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();

        mbtnUserRegister = findViewById(R.id.mbtn_user_register);
        mbtnUserRegister.setOnClickListener(this);
        mbtnUserSchedule = findViewById(R.id.mbtn_user_scheduler);
        mbtnUserSchedule.setOnClickListener(this);
        mbtnSimpleUser = findViewById(R.id.mbtn_simple_user);
        mbtnSimpleUser.setOnClickListener(this);
    }
}