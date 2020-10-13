package com.physis.foot.stretching.list.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;

public class PatternItemHolder extends RecyclerView.ViewHolder {

    public RelativeLayout layoutItem;
    public TextView tvOrder;
    public TextView tvLeftDirectionAngle, tvLeftMovingSpeed, tvLeftHoldingTime, tvLeftReturnable;
    public TextView tvRightDirectionAngle, tvRightMovingSpeed, tvRightHoldingTime, tvRightReturnable;

    public PatternItemHolder(@NonNull View view) {
        super(view);

        tvOrder = view.findViewById(R.id.tv_order);

        tvLeftDirectionAngle = view.findViewById(R.id.tv_left_direction_angle);
        tvLeftMovingSpeed = view.findViewById(R.id.tv_left_moving_speed);
        tvLeftHoldingTime = view.findViewById(R.id.tv_left_holding_time);
        tvLeftReturnable = view.findViewById(R.id.tv_left_returnable);


        tvRightDirectionAngle = view.findViewById(R.id.tv_right_direction_angle);
        tvRightMovingSpeed = view.findViewById(R.id.tv_right_moving_speed);
        tvRightHoldingTime = view.findViewById(R.id.tv_right_holding_time);
        tvRightReturnable = view.findViewById(R.id.tv_right_returnable);

        layoutItem = view.findViewById(R.id.frame_pattern_item);
    }
}
