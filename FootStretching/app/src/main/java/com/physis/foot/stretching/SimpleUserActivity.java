package com.physis.foot.stretching;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.physis.foot.stretching.data.HospitalInfo;
import com.physis.foot.stretching.data.PatternInfo;
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectedPattern(PatternInfo info) {
        fragment.setPatternCode(info.getCode());
    }

    @Override
    public void onEditPattern(PatternInfo info) {

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
        fragment = new StretchingFragment();
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