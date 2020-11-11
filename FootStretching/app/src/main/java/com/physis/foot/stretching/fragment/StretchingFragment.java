package com.physis.foot.stretching.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.dialog.LoadingDialog;
import com.physis.foot.stretching.http.HttpPacket;
import com.physis.foot.stretching.widget.MoveWinker;
import com.physis.foot.stretching.widget.PatternInfoCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class StretchingFragment extends MyBaseFragment implements View.OnClickListener{

    private TextView tvStateMsg;
    private PatternInfoCard picLeft, picRight;
    private MoveWinker mvLeft, mvRight;

    private String patternCode;
    private List<PatternItemInfo> patternItemInfos = new LinkedList<>();
    private int patternPos = 0;

    public void setPatternCode(String code){
        this.patternCode = code;
    }

    public StretchingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bleManager.registerReceiver();
        bleManager.setHandler(handler);
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

        picLeft = view.findViewById(R.id.pic_left);
        picRight = view.findViewById(R.id.pic_right);

        mvLeft = view.findViewById(R.id.mv_left);
        mvRight = view.findViewById(R.id.mv_right);

        tvStateMsg = view.findViewById(R.id.tv_state_msg);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bleManager.unregisterReceiver();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_move:
                break;
            case R.id.btn_stop_move:

                break;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onReadyToDevice() {
        super.onReadyToDevice();
        picLeft.initInfo();
        picRight.initInfo();
        tvStateMsg.setText("Zero 설정을 진행합니다.");
        sendControlMessage("ZS");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onReceiveAck(String msg) {
        super.onReceiveAck(msg);
        patternPos = msg.equals("ZS") ? 0 : patternPos + 1;
        if(patternPos != patternItemInfos.size()){
            PatternItemInfo info = patternItemInfos.get(patternPos);
            picLeft.setInfo(info.getLeftMoving());
            picRight.setInfo(info.getRightMoving());
            mvLeft.startBlink(info.getLeftMoving());
            mvRight.startBlink(info.getRightMoving());
            tvStateMsg.setText((patternPos + 1) + " 번째 Phase를 진행합니다..");
            sendControlMessage(info.getLeftMoving() + "/" + info.getRightMoving());
        }else{
            // finish
            sendControlMessage("FN");
            mvLeft.stopBlink();
            mvRight.stopBlink();
            tvStateMsg.setText("패턴운동이 종료되었습니다.");
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

}