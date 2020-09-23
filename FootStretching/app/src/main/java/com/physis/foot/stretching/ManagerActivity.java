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

import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.dialog.MyAlertDialog;
import com.physis.foot.stretching.helper.SwipeAndDragHelper;
import com.physis.foot.stretching.http.HttpAsyncTaskActivity;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.list.PatternAdapter;
import com.physis.foot.stretching.list.PatternItemAdapter;
import com.physis.foot.stretching.widget.PatternSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ManagerActivity extends HttpAsyncTaskActivity implements View.OnClickListener, PatternAdapter.OnSelectedPatternListener, PatternItemAdapter.OnSelectedPatternItemListener {

    private static final String TAG = ManagerActivity.class.getSimpleName();

    private EditText etSearchPattern, etPatternName, etPatterKeyword;
    private PatternSetter psLeftMoving, psRightMoving;
    private TextView tvNotifyPattern, tvNotifyPatternItem;

    private PatternAdapter patternAdapter;
    private PatternItemAdapter patternItemAdapter;

    private MyAlertDialog patterInfoDialog;

    private PatternInfo selectedPattern = null;
    private PatternItemInfo selectedPatternItem = null;
    private List<PatternItemInfo> patternItemInfoList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        init();
        getPatterList();
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        try {
            switch (url) {
                case HttpPacket.GET_PATTERNs_URL:
                    setPatternList(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.GET_PATTERN_ITEMs_URL:
                    setPatternItems(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                case HttpPacket.REGISTER_PATTERN_URL:
                    Toast.makeText(getApplicationContext(), "패턴운동 정보가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    getPatterList();
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
        } finally {
            patterInfoDialog.dismiss();
        }
    }

    @Override
    public void onSelectedPattern(PatternInfo info) {
        selectedPattern = info;
        getPatternItems(info.getCode());
    }

    @Override
    public void onEditPattern(PatternInfo info) {
        selectedPattern = info;
        showPatternInfoDialog();
    }

    @Override
    public void onPatternItemClick(PatternItemInfo info) {
        selectedPatternItem = info;
        if(selectedPatternItem != null){
            psLeftMoving.setMovingData(selectedPatternItem.getLeftMoving());
            psRightMoving.setMovingData(selectedPatternItem.getRightMoving());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_pattern:
                selectedPattern = null;
                showPatternInfoDialog();
                break;
            case R.id.btn_setup_pattern_item:
                setPatternInfo();
                break;
            case R.id.btn_update_pattern_item:
                updatePatternItems();
                break;
        }
    }

    private void updatePatternItems() {
        if(selectedPattern == null) {
            Toast.makeText(getApplicationContext(), "운동패턴을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<PatternItemInfo> infos = patternItemAdapter.getItems();

        if(infos.size() == 0){
            Toast.makeText(getApplicationContext(), "설정된 운동패턴 항목이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray params = new JSONArray();
        try{
            for(int i = 0; i < infos.size(); i++){
                JSONObject obj = new JSONObject();
                obj.put(HttpPacket.PARAMS_PATTERN_CODE, selectedPattern.getCode());
                obj.put(HttpPacket.PARAMS_ORDER, i + 1);
                obj.put(HttpPacket.PARAMS_ITEM_LEFT_MOVE, infos.get(i).getLeftMoving());
                obj.put(HttpPacket.PARAMS_ITEM_RIGHT_MOVE, infos.get(i).getRightMoving());
                params.put(obj);
            }
        }catch (JSONException e){
            e.getStackTrace();
        }
        requestAPI(HttpPacket.UPDATE_PATTERN_ITEMs_URL, params);
    }

    private void setPatternInfo(){
        if(selectedPattern == null) {
            Toast.makeText(getApplicationContext(), "운동패턴을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String leftMoving = psLeftMoving.getMovingData();
        String rightMoving = psRightMoving.getMovingData();

        if(leftMoving == null || leftMoving.isEmpty() || rightMoving == null || rightMoving.isEmpty())
            return;

        if(selectedPatternItem == null){
            patternItemInfoList.add(new PatternItemInfo(selectedPattern.getCode(),
                    patternItemInfoList.size() + 1,
                    psLeftMoving.getMovingData(),
                    psRightMoving.getMovingData()));
            tvNotifyPatternItem.setVisibility(View.GONE);
            patternItemAdapter.setItems(patternItemInfoList);
        }else{
            selectedPatternItem.setLeftMoving(leftMoving);
            selectedPatternItem.setRightMoving(rightMoving);
            patternItemAdapter.renewSelectItem();
        }
    }

    private void registerPattern(String name, String keyword){
        if(name == null || name.length() == 0){
            Toast.makeText(getApplicationContext(), "운동패턴 명칭을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_PATTERN_NAME, name);
            params.put(HttpPacket.PARAMS_PATTERN_KEYWORD, keyword);
            if(selectedPattern == null){
                requestAPI(HttpPacket.REGISTER_PATTERN_URL, params);
            }else{
                params.put(HttpPacket.PARAMS_PATTERN_CODE, selectedPattern.getCode());
                requestAPI(HttpPacket.UPDATE_PATTERN_URL, params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showPatternInfoDialog(){
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_pattern_info, null);

        final EditText etName = view.findViewById(R.id.et_pattern_name);
        final EditText etKeyword = view.findViewById(R.id.et_pattern_keyword);

        if(selectedPattern != null){
            etName.setText(selectedPattern.getName());
            etKeyword.setText(selectedPattern.getKeyword());
        }

        patterInfoDialog = new MyAlertDialog();
        patterInfoDialog.show(ManagerActivity.this, null, view,
                selectedPattern == null ? "등록" : "수정", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registerPattern(etName.getText().toString(), etKeyword.getText().toString());
                    }
                });
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
        patternItemAdapter.setItems(patternItemInfoList);
    }

    private void getPatternItems(String patternCode){
        psLeftMoving.initData();
        psRightMoving.initData();
        selectedPatternItem = null;

        try {
            JSONObject params = new JSONObject();
            params.put(HttpPacket.PARAMS_PATTERN_CODE, patternCode);
            requestAPI(HttpPacket.GET_PATTERN_ITEMs_URL, params);
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
        // Clear Info
        patternItemAdapter.clearItem();
        tvNotifyPatternItem.setVisibility(View.VISIBLE);
        selectedPattern = null;
        psLeftMoving.initData();
        psRightMoving.initData();

        requestAPI(HttpPacket.GET_PATTERNs_URL);
    }

    private void init() {
        etSearchPattern = findViewById(R.id.et_search_pattern);
        etPatternName = findViewById(R.id.et_pattern_name);
        etPatterKeyword = findViewById(R.id.et_pattern_keyword);

        Button btnAddPattern = findViewById(R.id.btn_add_pattern);
        btnAddPattern.setOnClickListener(this);
        Button btnSetPatternItems = findViewById(R.id.btn_setup_pattern_item);
        btnSetPatternItems.setOnClickListener(this);
        Button btnUpdatePatternItems = findViewById(R.id.btn_update_pattern_item);
        btnUpdatePatternItems.setOnClickListener(this);


        psLeftMoving = findViewById(R.id.ps_left_moving);
        psRightMoving = findViewById(R.id.ps_right_moving);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setItemPrefetchEnabled(true);
        RecyclerView rvPatterns = findViewById(R.id.rv_patterns);
        rvPatterns.setLayoutManager(layoutManager);
        rvPatterns.setAdapter(patternAdapter = new PatternAdapter());
        patternAdapter.setOnSelectedPatternListener(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager1.setItemPrefetchEnabled(true);
        RecyclerView rvPatternItems = findViewById(R.id.rv_pattern_items);
        rvPatternItems.setLayoutManager(layoutManager1);
        rvPatternItems.setAdapter(patternItemAdapter = new PatternItemAdapter());
        patternItemAdapter.setSelectedPatternItemListener(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new SwipeAndDragHelper(patternItemAdapter));
        patternItemAdapter.setTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(rvPatternItems);

        tvNotifyPattern = findViewById(R.id.tv_notify_pattern);
        tvNotifyPatternItem = findViewById(R.id.tv_notify_pattern_item);
    }

}