package com.physis.foot.stretching.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.widget.MoveWinker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class StretchingFragment extends MyBaseFragment implements View.OnClickListener{

    private TextView tvLeftAngle, tvLeftMovingTime, tvLeftHoldingTime;
    private TextView tvRightAngle, tvRightMovingTime, tvRightHoldingTime;
    private MoveWinker mvLeft, mvRight;

    private String patternCode;
    private List<PatternItemInfo> patternItemInfos = new LinkedList<>();

    public void setPatternCode(String code){
        this.patternCode = code;
    }

    public StretchingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stretching, container, false);

        Button btnStart = view.findViewById(R.id.btn_start_move);
        btnStart.setOnClickListener(this);
        Button btnStop = view.findViewById(R.id.btn_stop_move);
        btnStop.setOnClickListener(this);

        tvLeftAngle = view.findViewById(R.id.tv_left_direction_angle);
        tvLeftMovingTime = view.findViewById(R.id.tv_left_moving_speed);
        tvLeftHoldingTime = view.findViewById(R.id.tv_left_holding_time);

        tvRightAngle = view.findViewById(R.id.tv_right_direction_angle);
        tvRightMovingTime = view.findViewById(R.id.tv_right_moving_speed);
        tvRightHoldingTime = view.findViewById(R.id.tv_right_holding_time);

        mvLeft = view.findViewById(R.id.mv_left);
        mvRight = view.findViewById(R.id.mv_right);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_move:
                getPatternItems();
                break;
            case R.id.btn_stop_move:
                mvLeft.startBlink("3");
                break;
        }
    }

    @Override
    protected void onHttpResponse(String url, JSONObject resObj) {
        super.onHttpResponse(url, resObj);
        try {
            switch (url){
                case HttpPacket.GET_PATTERN_ITEMs_URL:
                    setPatternItems(resObj.getJSONArray(HttpPacket.PARAMS_ROWS));
                    break;
                default:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPatternItems(JSONArray rows){
        patternItemInfos.clear();
        for(int i = 0; i < rows.length(); i++){
            try {
                JSONObject obj = rows.getJSONObject(i);
                patternItemInfos.add(new PatternItemInfo(
                        obj.getString(HttpPacket.PARAMS_PATTERN_CODE),
                        obj.getInt(HttpPacket.PARAMS_ORDER),
                        obj.getString(HttpPacket.PARAMS_ITEM_LEFT_MOVE),
                        obj.getString(HttpPacket.PARAMS_ITEM_RIGHT_MOVE)
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPatternItems(){
        if(patternCode == null)
            return;
        JSONObject params = new JSONObject();
        try {
            params.put(HttpPacket.PARAMS_PATTERN_CODE, patternCode);
            requestAPI(HttpPacket.GET_PATTERN_ITEMs_URL, params);
            LoadingDialog.show(getActivity(), "Get Pattern items..");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}