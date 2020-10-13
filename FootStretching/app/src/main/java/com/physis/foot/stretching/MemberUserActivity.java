package com.physis.foot.stretching;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.physis.foot.stretching.fragment.StretchingFragment;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;

public class MemberUserActivity extends HttpAsyncTaskActivity {

    private TextView tvUserName, tvUserPhone, tvScheduleDate, tvPatternName;

    private StretchingFragment fragment;

    private String patternCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_user);

        init();
    }

    private void init() {
        fragment = new StretchingFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

        patternCode = getIntent().getStringExtra(HttpPacket.PARAMS_PATTERN_CODE);
        fragment.setPatternCode(patternCode);

        tvUserName = findViewById(R.id.tv_user_name);
        tvUserName.setText(getIntent().getStringExtra(HttpPacket.PARAMS_USER_NAME));
        tvUserPhone = findViewById(R.id.tv_user_phone);
        tvUserPhone.setText(getIntent().getStringExtra(HttpPacket.PARAMS_USER_PHONE));
        tvScheduleDate = findViewById(R.id.tv_schedule_date);
        tvScheduleDate.setText(getIntent().getStringExtra(HttpPacket.PARAMS_DATE_TIME));
        tvPatternName = findViewById(R.id.tv_pattern_name);
        tvPatternName.setText(getIntent().getStringExtra(HttpPacket.PARAMS_PATTERN_NAME));
    }
}