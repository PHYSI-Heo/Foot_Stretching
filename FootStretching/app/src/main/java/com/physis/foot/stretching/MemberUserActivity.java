package com.physis.foot.stretching;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.fragment.StretchingFragment;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.list.MotionItemAdapter;
import com.physis.foot.stretching.list.UserAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MemberUserActivity extends HttpAsyncTaskActivity {

    private StretchingFragment fragment;
    private String patternCode;
    private String scheduleNo;

    private List<PatternItemInfo> patternItemInfoList = new LinkedList<>();
    private MotionItemAdapter motionItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_user);
        init();

        getPatternItems();
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        try {
            switch (url){
                case HttpPacket.GET_PATTERN_ITEMs_URL:
                    setPatternItems(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.UPDATE_SCHEDULE_URL:
                    Toast.makeText(getApplicationContext(), "운통 일정이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            LoadingDialog.dismiss();
        }
    }


    private void setPatternItems(JSONArray rows){
        patternItemInfoList.clear();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                patternItemInfoList.add(new PatternItemInfo(
                        obj.getString(HttpPacket.PARAMS_PATTERN_CODE),
                        obj.getInt(HttpPacket.PARAMS_ORDER),
                        obj.getString(HttpPacket.PARAMS_ITEM_LEFT_MOVE),
                        obj.getString(HttpPacket.PARAMS_ITEM_RIGHT_MOVE)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        motionItemAdapter.setItems(patternItemInfoList);
        fragment.setPatternItems(patternItemInfoList);
    }

    private void getPatternItems(){
        if(patternCode == null)
            return;

        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_PATTERN_CODE, patternCode);
            requestAPI(HttpPacket.GET_PATTERN_ITEMs_URL, params);
            LoadingDialog.show(this, "Get pattern item list..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setScheduleState(){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_NO, scheduleNo);
            params.put(HttpPacket.PARAMS_FULFILL, true);
            requestAPI(HttpPacket.UPDATE_SCHEDULE_URL, params);
            LoadingDialog.show(this, "Get pattern item list..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        fragment = StretchingFragment.newInstance(StretchingFragment.FRAGMENT_USER);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

        patternCode = getIntent().getStringExtra(HttpPacket.PARAMS_PATTERN_CODE);

        TextView tvUserName = findViewById(R.id.tv_user_name);
        tvUserName.setText(getIntent().getStringExtra(HttpPacket.PARAMS_USER_NAME));
        TextView tvUserPhone = findViewById(R.id.tv_user_phone);
        tvUserPhone.setText(getIntent().getStringExtra(HttpPacket.PARAMS_USER_PHONE));
        TextView tvScheduleDate = findViewById(R.id.tv_schedule_date);
        tvScheduleDate.setText(getIntent().getStringExtra(HttpPacket.PARAMS_DATE_TIME));
        TextView tvPatternName = findViewById(R.id.tv_pattern_name);
        tvPatternName.setText(getIntent().getStringExtra(HttpPacket.PARAMS_PATTERN_NAME));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setItemPrefetchEnabled(true);
        RecyclerView reMotions = findViewById(R.id.rc_motions);
        reMotions.setLayoutManager(layoutManager);
        reMotions.setAdapter(motionItemAdapter = new MotionItemAdapter());

        scheduleNo = getIntent().getStringExtra(HttpPacket.PARAMS_NO);
    }
}