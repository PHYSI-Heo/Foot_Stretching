package com.physis.foot.stretching.list.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;

public class MotionItemHolder extends RecyclerView.ViewHolder {

    public TextView tvLeftDir, tvLeftAngle, tvLeftSpeed, tvLeftHolding;
    public TextView tvRightDir, tvRightAngle, tvRightSpeed, tvRightHolding;

    public MotionItemHolder(@NonNull View itemView) {
        super(itemView);

        tvLeftDir = itemView.findViewById(R.id.tv_left_dir);
        tvLeftAngle = itemView.findViewById(R.id.tv_left_angle);
        tvLeftSpeed = itemView.findViewById(R.id.tv_left_speed);
        tvLeftHolding = itemView.findViewById(R.id.tv_left_holding);

        tvRightDir = itemView.findViewById(R.id.tv_right_dir);
        tvRightAngle = itemView.findViewById(R.id.tv_right_angle);
        tvRightSpeed = itemView.findViewById(R.id.tv_right_speed);
        tvRightHolding = itemView.findViewById(R.id.tv_right_holding);
    }
}
