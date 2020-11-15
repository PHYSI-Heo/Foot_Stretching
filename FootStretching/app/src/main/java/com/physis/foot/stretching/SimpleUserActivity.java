package com.physis.foot.stretching;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.physis.foot.stretching.ble.BluetoothLEManager;
import com.physis.foot.stretching.data.HospitalInfo;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.fragment.StretchingFragment;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.list.PatternAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class SimpleUserActivity extends HttpAsyncTaskActivity implements PatternAdapter.OnSelectedPatternListener {

    private PatternAdapter patternAdapter;
    private TextView tvNotifyPattern;

    private StretchingFragment fragment;

    private List<PatternItemInfo> patternItemInfoList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_user);

        init();
        getPatterList();
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        LoadingDialog.dismiss();
        try {
            if(url.equals(HttpPacket.GET_MY_PATTERNs_URL)) {
                setPatternList(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
            }else if(url.equals(HttpPacket.GET_PATTERN_ITEMs_URL)){
                setPatternItems(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                Toast.makeText(getApplicationContext(), "패턴운동 정보를 획득하였습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectedPattern(PatternInfo info) {
        getPatternItems(info.getCode());
    }

    @Override
    public void onEditPattern(PatternInfo info) {

    }

    public void setPatternClickable(boolean clickable){
        patternAdapter.setItemClickable(clickable);
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
        fragment.setPatternItems(patternItemInfoList);
    }

    private void getPatternItems(String patternCode){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_PATTERN_CODE, patternCode);
            requestAPI(HttpPacket.GET_PATTERN_ITEMs_URL, params);
            LoadingDialog.show(this, "Get pattern item list..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPatternList(JSONArray rows){
        List<PatternInfo> infos = new LinkedList<>();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                infos.add(new PatternInfo(
                        obj.getString(HttpPacket.PARAMS_PATTERN_CODE),
                        obj.getString(HttpPacket.PARAMS_PATTERN_NAME),
                        obj.getString(HttpPacket.PARAMS_PATTERN_KEYWORD)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvNotifyPattern.setVisibility(infos.size() == 0 ? View.VISIBLE : View.GONE);
        patternAdapter.setItems(infos);
    }

    private void getPatterList(){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_HOSPITAL_CODE, HospitalInfo.getInstance().getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestAPI(HttpPacket.GET_MY_PATTERNs_URL, params);
        LoadingDialog.show(SimpleUserActivity.this, "Get Patterns..");
    }

    private void init() {
        fragment = StretchingFragment.newInstance(StretchingFragment.FRAGMENT_SIMPLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setItemPrefetchEnabled(true);
        RecyclerView rcPattern = findViewById(R.id.rv_patterns);
        rcPattern.setLayoutManager(layoutManager);
        rcPattern.setAdapter(patternAdapter = new PatternAdapter());
        patternAdapter.setOnSelectedPatternListener(this);

        tvNotifyPattern = findViewById(R.id.tv_notify_pattern);
    }

}