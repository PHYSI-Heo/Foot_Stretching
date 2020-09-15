package com.physis.foot.stretching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private static final long INTRO_DELAY = 1500;
    private static final int REQ_APP_PERMISSION = 1500;

    private String[] appPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
    };

    private Handler introHandler = new Handler(Looper.getMainLooper());
    private Runnable introRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_APP_PERMISSION){
            boolean accessStatus = true;
            for(int grantResult : grantResults){
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    accessStatus = false;
                    break;
                }
            }
            if(!accessStatus){
                Toast.makeText(getApplicationContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                nextActivity();
            }
        }
    }

    @Override
    public void onBackPressed() {
        introHandler.removeCallbacks(introRunnable);
        super.onBackPressed();
    }

    private void checkPermissions(){
        final List<String> reqPermissions = new ArrayList<>();
        for(String permission : appPermissions){
            if(checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
                reqPermissions.add(permission);
            }
        }
        if(reqPermissions.size() == 0){
            nextActivity();
        }else{
            requestPermissions(reqPermissions.toArray(new String[reqPermissions.size()]), REQ_APP_PERMISSION);
        }
    }

    private void nextActivity(){
        introHandler.postDelayed(introRunnable, INTRO_DELAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        checkPermissions();
        init();
    }

    private void init() {
        AnimationDrawable animDrawable = (AnimationDrawable)findViewById(R.id.layout_frame).getBackground();
        animDrawable.setEnterFadeDuration(4500);
        animDrawable.setExitFadeDuration(4500);
        animDrawable.start();
    }
}