package com.physis.foot.stretching.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.physis.foot.stretching.R;

public class PatternSetter extends LinearLayout {

    private EditText etVerticalAngle, etHorizontalAngle, etMovingSpeed, etHoldingTime;
    private RadioButton rbtnUp, rbtnDown, rbtnLeft, rbtnRight;
    private CheckBox cbReturnable;

    private Context context;

    public PatternSetter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_pettern_setter, this, false);
        addView(view);

        etVerticalAngle = view.findViewById(R.id.et_vertical_angle);
        etHorizontalAngle = view.findViewById(R.id.et_horizontal_angle);
        etMovingSpeed = view.findViewById(R.id.et_moving_speed);
        etHoldingTime = view.findViewById(R.id.et_holding_time);

        rbtnUp = view.findViewById(R.id.rbtn_angle_up);
        rbtnDown = view.findViewById(R.id.rbtn_angle_down);
        rbtnLeft = view.findViewById(R.id.rbtn_angle_left);
        rbtnRight = view.findViewById(R.id.rbtn_angle_right);

        cbReturnable = view.findViewById(R.id.cb_returnable);
    }

    public String getMovingData(){
        String verticalAngle = etVerticalAngle.getText().toString();
        String horizontalAngle = etHorizontalAngle.getText().toString();
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

        if((verticalAngle.isEmpty() || verticalAngle.equals("0")) && !horizontalAngle.isEmpty() && !horizontalAngle.equals("0")){
            // Left & Right
            return (rbtnLeft.isChecked() ? "3," : "4,") + horizontalAngle + "," + (cbReturnable.isChecked() ? "1," : "0,") + movingSpeed + "," + holdingTime;
        }

        if((horizontalAngle.isEmpty() || horizontalAngle.equals("0")) && !verticalAngle.isEmpty() && !verticalAngle.equals("0")){
            // Up & Down
            return (rbtnUp.isChecked() ? "1," : "2,") + verticalAngle + "," + (cbReturnable.isChecked() ? "1," : "0,") + movingSpeed + "," + holdingTime;
        }

        Toast.makeText(context, "한번에 상하 또는 좌우 각도 하나만 설정 가능합니다.", Toast.LENGTH_SHORT).show();
        return null;
    }

    public void initData(){
        etVerticalAngle.setText(null);
        etHorizontalAngle.setText(null);
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

    private void setDirectionAngle(String direction, String angle){
        switch (direction){
            case "1":
                rbtnUp.setChecked(true);
                rbtnLeft.setChecked(true);
                etVerticalAngle.setText(angle);
                etHorizontalAngle.setText("0");
                break;
            case "2":
                rbtnDown.setChecked(true);
                rbtnLeft.setChecked(true);
                etVerticalAngle.setText(angle);
                etHorizontalAngle.setText("0");
                break;
            case "3":
                rbtnLeft.setChecked(true);
                rbtnUp.setChecked(true);
                etVerticalAngle.setText("0");
                etHorizontalAngle.setText(angle);
                break;
            case "4":
                rbtnRight.setChecked(true);
                rbtnUp.setChecked(true);
                etVerticalAngle.setText("0");
                etHorizontalAngle.setText(angle);
                break;
        }
    }
}
