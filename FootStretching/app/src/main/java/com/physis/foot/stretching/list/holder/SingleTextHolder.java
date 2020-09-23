package com.physis.foot.stretching.list.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;

public class SingleTextHolder extends RecyclerView.ViewHolder {

    public TextView tvTxt;

    public SingleTextHolder(@NonNull View view) {
        super(view);

        tvTxt = view.findViewById(R.id.layout_txt_item);
    }
}
