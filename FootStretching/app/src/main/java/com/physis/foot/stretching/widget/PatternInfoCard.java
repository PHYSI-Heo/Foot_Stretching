package com.physis.foot.stretching.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.helper.SystemEnv;

public class PatternInfoCard extends RelativeLayout {

    private TextView tvDirAngle, tvMovingSpeed, tvHoldingTime;
    private ImageView ivReturnIcon;


    public PatternInfoCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.widget_pattern_info_card, this, false);
        addView(view);

        tvDirAngle = view.findViewById(R.id.tv_direction_angle);
        tvMovingSpeed = view.findViewById(R.id.tv_moving_speed);
        tvHoldingTime = view.findViewById(R.id.tv_holding_time);

        ivReturnIcon = view.findViewById(R.id.iv_return_icon);
    }

    @SuppressLint("SetTextI18n")
    public void setInfo(String info){
        String[] infos = info.split(",");
        if(infos.length != 5)
            return;

        String dir = infos[0].equals("1") ? "Up" : (infos[0].equals("2") ? "Down" : infos[0].equals("3") ? "Left" : "Right");

        tvDirAngle.setText("Direction > " + dir + " ( " + SystemEnv.getAnglePhaseValue(infos[0], infos[1]) + "° )");
        tvMovingSpeed.setText("Moving Speed > " + infos[3]);
        tvHoldingTime.setText("Holding Time > " + infos[4]);

        ivReturnIcon.setBackgroundResource(infos[2].equals("0") ? R.drawable.ic_return_disable : R.drawable.ic_return_enable);
    }

    @SuppressLint("SetTextI18n")
    public void initInfo(){
        tvDirAngle.setText("Direction > -- ( --° )");
        tvMovingSpeed.setText("Moving Speed > --");
        tvHoldingTime.setText("Holding Time > -- Sec");
        ivReturnIcon.setBackgroundResource(R.drawable.ic_return_disable);
    }

}
