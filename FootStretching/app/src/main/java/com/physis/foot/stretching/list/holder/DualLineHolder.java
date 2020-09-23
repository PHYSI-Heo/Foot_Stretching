package com.physis.foot.stretching.list.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;

public class DualLineHolder extends RecyclerView.ViewHolder {

    public LinearLayout itemFrame;
    public TextView tvMainLine, tvSubLine;

    public DualLineHolder(@NonNull View view) {
        super(view);

        itemFrame = view.findViewById(R.id.layout_schedule_item);
        tvMainLine = view.findViewById(R.id.tv_main_line);
        tvSubLine = view.findViewById(R.id.tv_sub_line);
    }
}
