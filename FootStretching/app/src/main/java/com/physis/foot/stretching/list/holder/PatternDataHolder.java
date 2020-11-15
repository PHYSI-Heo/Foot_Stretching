package com.physis.foot.stretching.list.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;

public class PatternDataHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvSubData, tvExplanation;
    public RelativeLayout itemLayout;
    public ImageView ivBtnDetail, ivBtnAdd;

    public PatternDataHolder(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tv_pattern_name);
        tvSubData = itemView.findViewById(R.id.tv_sub_info);
        tvExplanation = itemView.findViewById(R.id.tv_explanation);

        itemLayout = itemView.findViewById(R.id.layout_pattern_data);

        ivBtnDetail = itemView.findViewById(R.id.iv_btn_detail);
        ivBtnAdd = itemView.findViewById(R.id.iv_btn_add);
    }
}
