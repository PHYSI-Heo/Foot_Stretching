package com.physis.foot.stretching.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.SimpleUserActivity;
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

    private static final String TAG = StretchingFragment.class.getSimpleName();

    private final static String FRAGMENT_TYPE = "F_TYPE";
    public final static int FRAGMENT_USER = 11;
    public final static int FRAGMENT_SIMPLE = 12;

    private TextView tvStateMsg;
    private PatternInfoCard picLeft, picRight;
    private MoveWinker mvLeft, mvRight;

    private List<PatternItemInfo> patternItemInfoList = new LinkedList<>();
    private int patternPos = 0;
    private int fragmentType = -1;

    public void setPatternItems(List<PatternItemInfo> infoList){
        this.patternItemInfoList = infoList;
    }

    public StretchingFragment() {
        // Required empty public constructor
    }

    public static StretchingFragment newInstance(int code){
        StretchingFragment fragment = new StretchingFragment();
        Bundle args =  new Bundle();
        args.putInt(FRAGMENT_TYPE, code);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bleManager.registerReceiver();
        bleManager.setHandler(handler);
        if (getArguments() != null) {
            fragmentType = getArguments().getInt(FRAGMENT_TYPE);
        }
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
                connectBoard();
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
        if(fragmentType == FRAGMENT_SIMPLE){
            ((SimpleUserActivity)getActivity()).setPatternClickable(false);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onReceiveAck(String msg) {
        super.onReceiveAck(msg);
        ackHandler(msg);
    }

    private void ackHandler(String msg){
        patternPos = msg.equals("ZS") ? 0 : patternPos + 1;
        if(patternPos < patternItemInfoList.size()){
            PatternItemInfo info = patternItemInfoList.get(patternPos);
            picLeft.setInfo(info.getLeftMoving());
            picRight.setInfo(info.getRightMoving());
            mvLeft.startBlink(info.getLeftMoving());
            mvRight.startBlink(info.getRightMoving());
            tvStateMsg.setText((patternPos + 1) + " 번째 Phase를 진행합니다..");
            sendControlMsg(info.getLeftMoving(), info.getRightMoving());
        }else if(msg.equals("FN")){
            if(fragmentType == FRAGMENT_SIMPLE){
                ((SimpleUserActivity)getActivity()).setPatternClickable(false);
            }else{
                // Update State
            }
        }else{
            // finish
            sendControlMessage("FN");
            picLeft.initInfo();
            picRight.initInfo();
            mvLeft.stopBlink();
            mvRight.stopBlink();
            tvStateMsg.setText("패턴운동이 종료되었습니다.");
        }
    }

    private void sendControlMsg(String leftMotion, String rightMotion){
        String[] leftCons = leftMotion.split(",");
        String[] rightCons = rightMotion.split(",");
        String conMsg = leftCons[0] + leftCons[2] + leftCons[4] + "," +  leftCons[3] + "," + leftCons[1] + "/";
        conMsg += rightCons[0] + rightCons[2] + rightCons[4] + "," + rightCons[3] + "," + rightCons[1];
        sendControlMessage(conMsg);
    }


}