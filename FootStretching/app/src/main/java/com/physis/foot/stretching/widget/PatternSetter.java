package com.physis.foot.stretching.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.physis.foot.stretching.R;

import java.util.LinkedList;
import java.util.List;

public class PatternSetter extends LinearLayout {

    private static final String TAG = PatternSetter.class.getSimpleName();

    private EditText etMovingSpeed, etHoldingTime;
    private RadioButton rbtnUp, rbtnDown, rbtnLeft, rbtnRight;
    private RadioButton rbtnVertical0, rbtnVertical1, rbtnVertical2, rbtnVertical3;
    private RadioButton rbtnHorizontal0, rbtnHorizontal1, rbtnHorizontal2;
    private CheckBox cbReturnable;

    private Context context;

    private List<RadioButton> rbtnVerticals = new LinkedList<>();
    private List<RadioButton> rbtnHorizontals = new LinkedList<>();

    public PatternSetter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_pattern_setter, this, false);
        addView(view);

        rbtnVertical0 = view.findViewById(R.id.rbtn_vertical_0);
        rbtnVerticals.add(rbtnVertical0);
        rbtnVertical1 = view.findViewById(R.id.rbtn_vertical_1);
        rbtnVerticals.add(rbtnVertical1);
        rbtnVertical2 = view.findViewById(R.id.rbtn_vertical_2);
        rbtnVerticals.add(rbtnVertical2);
        rbtnVertical3 = view.findViewById(R.id.rbtn_vertical_3);
        rbtnVerticals.add(rbtnVertical3);

        rbtnHorizontal0 = view.findViewById(R.id.rbtn_horizontal_0);
        rbtnHorizontals.add(rbtnHorizontal0);
        rbtnHorizontal1 = view.findViewById(R.id.rbtn_horizontal_1);
        rbtnHorizontals.add(rbtnHorizontal1);
        rbtnHorizontal2 = view.findViewById(R.id.rbtn_horizontal_2);
        rbtnHorizontals.add(rbtnHorizontal2);

        etMovingSpeed = view.findViewById(R.id.et_moving_speed);
        etHoldingTime = view.findViewById(R.id.et_holding_time);

        rbtnUp = view.findViewById(R.id.rbtn_angle_up);
        rbtnDown = view.findViewById(R.id.rbtn_angle_down);
        rbtnLeft = view.findViewById(R.id.rbtn_angle_left);
        rbtnRight = view.findViewById(R.id.rbtn_angle_right);

        cbReturnable = view.findViewById(R.id.cb_returnable);
    }

    public String getMovingData(){
        String movingSpeed = etMovingSpeed.getText().toString();
        String holdingTime = etHoldingTime.getText().toString();

        if(movingSpeed.isEmpty() || holdingTime.isEmpty()){
            Toast.makeText(context, "이동 속도 및 유지 시간을 설정하세요.", Toast.LENGTH_SHORT).show();
            return null;
        }

        int speedNum = Integer.parseInt(movingSpeed);

        if(speedNum < 0 || speedNum > 5){
            Toast.makeText(context, "이동 속도 단계를 확인하세요.", Toast.LENGTH_SHORT).show();
            return null;
        }

        if(!rbtnVertical0.isChecked() && !rbtnHorizontal0.isChecked()){
            Toast.makeText(context, "한번에 상하 또는 좌우 각도 하나만 설정 가능합니다.", Toast.LENGTH_SHORT).show();
            return null;
        }


        if(rbtnVertical0.isChecked()){
            // Left & Right
            String angle = rbtnHorizontal0.isChecked() ? "0" : (rbtnHorizontal1.isChecked() ? "1" : (rbtnHorizontal2.isChecked() ? "2" : "3"));
            return (rbtnLeft.isChecked() ? "3," : "4,") + angle + "," + (cbReturnable.isChecked() ? "1," : "0,") + movingSpeed + "," + holdingTime;
        }else{
            // Up & Down
            String angle = rbtnVertical0.isChecked() ? "0" : (rbtnVertical1.isChecked() ? "1" : (rbtnVertical2.isChecked() ? "2" : "3"));
            return (rbtnUp.isChecked() ? "1," : "2,") + angle + "," + (cbReturnable.isChecked() ? "1," : "0,") + movingSpeed + "," + holdingTime;
        }
    }


    public void initData(){
        rbtnVertical0.setChecked(true);
        rbtnHorizontal0.setChecked(true);
        etMovingSpeed.setText(null);
        etHoldingTime.setText(null);
    }

    public void setMovingData(String data){
        String[] datas = data.split(",");
        setDirectionAngle(datas[0], datas[1]);
        cbReturnable.setChecked(datas[2].equals("1"));
        etMovingSpeed.setText(datas[3]);
        etHoldingTime.setText(datas[4]);
    }

    private void setDirectionAngle(String direction, String anglePhase){
        switch (direction){
            case "1":
                rbtnUp.setChecked(true);
                rbtnLeft.setChecked(true);
                rbtnVerticals.get(Integer.parseInt(anglePhase)).setChecked(true);
                rbtnHorizontal0.setChecked(true);
                break;
            case "2":
                rbtnDown.setChecked(true);
                rbtnLeft.setChecked(true);
                rbtnVerticals.get(Integer.parseInt(anglePhase)).setChecked(true);
                rbtnHorizontal0.setChecked(true);
                break;
            case "3":
                rbtnLeft.setChecked(true);
                rbtnUp.setChecked(true);
                rbtnVertical0.setChecked(true);
                rbtnHorizontals.get(Integer.parseInt(anglePhase)).setChecked(true);
                break;
            case "4":
                rbtnRight.setChecked(true);
                rbtnUp.setChecked(true);
                rbtnVertical0.setChecked(true);
                rbtnHorizontals.get(Integer.parseInt(anglePhase)).setChecked(true);
                break;
        }
    }
}
