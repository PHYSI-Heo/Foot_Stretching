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
    public TextView tvLeftDirectionAngle, tvLeftMovingTime, tvLeftHoldingTime, tvLeftReturnable;
    public TextView tvRightDirectionAngle, tvRightMovingTime, tvRightHoldingTime, tvRightReturnable;

    public PatternItemHolder(@NonNull View view) {
        super(view);

        tvOrder = view.findViewById(R.id.tv_order);

        tvLeftDirectionAngle = view.findViewById(R.id.tv_left_direction_angle);
        tvLeftMovingTime = view.findViewById(R.id.tv_left_moving_time);
        tvLeftHoldingTime = view.findViewById(R.id.tv_left_holding_time);
        tvLeftReturnable = view.findViewById(R.id.tv_left_returnable);


        tvRightDirectionAngle = view.findViewById(R.id.tv_right_direction_angle);
        tvRightMovingTime = view.findViewById(R.id.tv_right_moving_time);
        tvRightHoldingTime = view.findViewById(R.id.tv_right_holding_time);
        tvRightReturnable = view.findViewById(R.id.tv_right_returnable);

        layoutItem = view.findViewById(R.id.frame_pattern_item);
    }
}
