package com.physis.foot.stretching.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.ScheduleInfo;
import com.physis.foot.stretching.list.holder.DualLineHolder;

import java.util.LinkedList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<DualLineHolder> {

    public interface OnSelectedScheduleListener{
        void onScheduleInfo(int position);
        void onStartSchedule(ScheduleInfo info);
    }

    private OnSelectedScheduleListener listener;

    public void setOnSelectedScheduleListener(OnSelectedScheduleListener listener){
        this.listener = listener;
    }

    private List<ScheduleInfo> scheduleInfos = new LinkedList<>();
    private int selectedPosition = -1;
    private int oldPosition = -1;

    @NonNull
    @Override
    public DualLineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dual_line_text, parent, false);
        return new DualLineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DualLineHolder holder, final int position) {
        final ScheduleInfo info = scheduleInfos.get(position);

        holder.tvMainLine.setText(info.getDateTime());
        holder.tvSubLine.setText(info.getPatternName());

        if(info.getFulfill()){
            holder.itemFrame.setBackgroundResource(R.color.colorDisable);
        }

        holder.itemFrame.setBackgroundResource(
                selectedPosition == position ? R.color.colorSelectedPatterns : R.color.colorPatterns);

        holder.itemFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition == position){
                    selectedPosition = -1;
                    notifyItemChanged(position);
                }else{
                    oldPosition = selectedPosition;
                    selectedPosition = position;
                    notifyItemChanged(selectedPosition);
                    if(oldPosition != -1)
                        notifyItemChanged(oldPosition);
                }

                if(listener != null)
                    listener.onScheduleInfo(selectedPosition);
            }
        });

        holder.itemFrame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener != null)
                    listener.onStartSchedule(info);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleInfos.size();
    }

    public void setItems(List<ScheduleInfo> infos){
        this.scheduleInfos = infos;
        notifyDataSetChanged();
    }

    public void renewSelectItem() {
        notifyItemChanged(selectedPosition);
    }
}
