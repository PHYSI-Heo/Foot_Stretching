package com.physis.foot.stretching.list;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.helper.SystemEnv;
import com.physis.foot.stretching.list.holder.DualLineHolder;
import com.physis.foot.stretching.list.holder.MotionItemHolder;

import java.util.LinkedList;
import java.util.List;

public class MotionItemAdapter extends RecyclerView.Adapter<MotionItemHolder> {


    private List<PatternItemInfo> itemList = new LinkedList<>();

    @NonNull
    @Override
    public MotionItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_motion_row, parent, false);
        return new MotionItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MotionItemHolder holder, int position) {
        PatternItemInfo info = itemList.get(position);

        String[] leftDatas = info.getLeftMoving().split(",");
        String[] rightDatas = info.getRightMoving().split(",");

        holder.tvLeftDir.setText(SystemEnv.getDirectionStr(leftDatas[0]));
        holder.tvLeftAngle.setText(leftDatas[1] + " °");
        holder.tvLeftSpeed.setText(leftDatas[2] + " Phase");
        holder.tvLeftHolding.setText(leftDatas[3] + " Sec");

        holder.tvRightDir.setText(SystemEnv.getDirectionStr(rightDatas[0]));
        holder.tvRightAngle.setText(rightDatas[1] + " °");
        holder.tvRightSpeed.setText(rightDatas[2] + " Phase");
        holder.tvRightHolding.setText(rightDatas[3] + " Sec");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<PatternItemInfo> items){
        this.itemList = items;
        notifyDataSetChanged();
    }
}
