package com.physis.foot.stretching.list.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;

public class UserHolder extends RecyclerView.ViewHolder {

    public RelativeLayout itemFrame;
    public ImageView ivPersonIcon;
    public TextView tvName, tvPhone;

    public UserHolder(@NonNull View view) {
        super(view);

        itemFrame = view.findViewById(R.id.layout_user_item);
        ivPersonIcon = view.findViewById(R.id.iv_person);
        tvName = view.findViewById(R.id.tv_name);
        tvPhone = view.findViewById(R.id.tv_phone);
    }
}
