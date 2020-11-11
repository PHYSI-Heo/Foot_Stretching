package com.physis.foot.stretching;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.physis.foot.stretching.data.HospitalInfo;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.dialog.MyAlertDialog;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.list.MotionItemAdapter;
import com.physis.foot.stretching.list.PatternAdapter;
import com.physis.foot.stretching.list.PatternItemAdapter;
import com.physis.foot.stretching.widget.PatternSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ManagerActivity extends HttpAsyncTaskActivity implements View.OnClickListener, PatternAdapter.OnSelectedPatternListener {

    private static final String TAG = ManagerActivity.class.getSimpleName();

    private EditText etSearchPattern;
    private TextView tvNotifyPattern, tvNotifyPatternItem, tvNotifyMyPattern;

    private PatternAdapter myPatternAdapter;
    private MotionItemAdapter motionItemAdapter;

    private PatternInfo selectedPattern = null;
    private List<PatternInfo> patternInfoList = new LinkedList<>();
    private List<PatternItemInfo> patternItemInfoList = new LinkedList<>();
    private List<PatternInfo> myPatternList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        init();
        getPatterList();
        getMyPatterList();
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        LoadingDialog.dismiss();
        try {
            switch (url) {
                case HttpPacket.GET_PATTERN_URL:
                    setPatternList(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.GET_PATTERN_ITEMs_URL:
                    setPatternItems(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.GET_MY_PATTERNs_URL:
                    setMyPatternList(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.UPDATE_PATTERN_URL:
                    Toast.makeText(getApplicationContext(), "패턴운동 정보가 갱신되었습니다.", Toast.LENGTH_SHORT).show();
                    getPatterList();
                    break;
                case HttpPacket.UPDATE_PATTERN_ITEMs_URL:
                    Toast.makeText(getApplicationContext(), "패턴운동 항목이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                    getPatternItems(selectedPattern.getCode());
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelectedPattern(PatternInfo info) {
        selectedPattern = info;
        getPatternItems(info.getCode());
    }

    @Override
    public void onEditPattern(PatternInfo info) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_setup:
                break;
        }
    }


    private void setMyPatternList(JSONArray rows){
        myPatternList.clear();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                myPatternList.add(new PatternInfo(
                        obj.getString(HttpPacket.PARAMS_PATTERN_CODE),
                        obj.getString(HttpPacket.PARAMS_PATTERN_NAME),
                        obj.getString(HttpPacket.PARAMS_PATTERN_KEYWORD)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvNotifyMyPattern.setVisibility(myPatternList.size() == 0 ? View.VISIBLE : View.GONE);
        myPatternAdapter.setItems(myPatternList);
    }

    private void getMyPatterList(){
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_HOSPITAL_CODE, HospitalInfo.getInstance().getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestAPI(HttpPacket.GET_MY_PATTERNs_URL, params);
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
        tvNotifyPatternItem.setVisibility(patternItemInfoList.size() == 0 ? View.VISIBLE : View.GONE);
        motionItemAdapter.setItems(patternItemInfoList);
    }

    private void getPatternItems(String patternCode){
        try {
            JSONObject params = new JSONObject();
            params.put(HttpPacket.PARAMS_PATTERN_CODE, patternCode);
            requestAPI(HttpPacket.GET_PATTERN_ITEMs_URL, params);
            LoadingDialog.show(ManagerActivity.this, "Get pattern motions..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPatternList(JSONArray rows){
        patternInfoList.clear();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                patternInfoList.add(new PatternInfo(
                        obj.getString(HttpPacket.PARAMS_PATTERN_CODE),
                        obj.getString(HttpPacket.PARAMS_PATTERN_NAME),
                        obj.getString(HttpPacket.PARAMS_PATTERN_EXPLANATION),
                        obj.getString(HttpPacket.PARAMS_PATTERN_KEYWORD),
                        obj.getString(HttpPacket.PARAMS_HOSPITAL_NAME),
                        obj.getString(HttpPacket.PARAMS_PATTERN_RUN_TIME)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvNotifyPattern.setVisibility(patternInfoList.size() == 0 ? View.VISIBLE : View.GONE);
        patternAdapter.setItems(patternInfoList);
    }

    private void getPatterList(){
        patternInfoList.clear();
        patternItemInfoList.clear();
        selectedPattern = null;
        requestAPI(HttpPacket.GET_PATTERN_URL, (JSONObject) null);
        LoadingDialog.show(ManagerActivity.this, "Get patterns..");
    }

    private void init() {
        etSearchPattern = findViewById(R.id.et_search_pattern);

        Button btnSetup = findViewById(R.id.btn_setup);
        btnSetup.setOnClickListener(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager1.setItemPrefetchEnabled(true);
        RecyclerView rvPatterns = findViewById(R.id.rv_patterns);
        rvPatterns.setLayoutManager(layoutManager1);
        
        rvPatterns.setAdapter(patternAdapter = new PatternAdapter());
        patternAdapter.setOnSelectedPatternListener(this);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager2.setItemPrefetchEnabled(true);
        RecyclerView rvPatternItems = findViewById(R.id.rv_pattern_items);
        rvPatternItems.setLayoutManager(layoutManager2);
        rvPatternItems.setAdapter(motionItemAdapter = new MotionItemAdapter());


        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext());
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager3.setItemPrefetchEnabled(true);
        RecyclerView rvMyPattern = findViewById(R.id.rv_my_patterns);
        rvMyPattern.setLayoutManager(layoutManager3);
        rvMyPattern.setAdapter(myPatternAdapter = new PatternAdapter());

        tvNotifyPattern = findViewById(R.id.tv_notify_pattern);
        tvNotifyPatternItem = findViewById(R.id.tv_notify_pattern_item);
        tvNotifyMyPattern = findViewById(R.id.tv_notify_my_pattern);
    }

}