package com.physis.foot.stretching.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.list.holder.DualLineHolder;

import java.util.LinkedList;
import java.util.List;

public class PatternAdapter extends RecyclerView.Adapter<DualLineHolder> {

    public interface OnSelectedPatternListener{
        void onSelectedPattern(PatternInfo info);
        void onEditPattern(PatternInfo info);
    }

    private OnSelectedPatternListener listener;

    public void setOnSelectedPatternListener(OnSelectedPatternListener listener){
        this.listener = listener;
    }


    private List<PatternInfo> patterns = new LinkedList<>();
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
        final PatternInfo info = patterns.get(position);

        holder.tvMainLine.setText(info.getName());
        holder.tvSubLine.setText(info.getKeyword().isEmpty() ? "NULL" : info.getKeyword());

        holder.itemFrame.setBackgroundResource(
                selectedPosition == position ? R.color.colorSelectedPatterns : R.color.colorPatterns);

        holder.itemFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition == position)
                    return;

                setSelectedPosition(position);

                if(listener != null)
                    listener.onSelectedPattern(info);
            }
        });

        holder.itemFrame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener != null)
                    listener.onEditPattern(info);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return patterns.size();
    }

    private void setSelectedPosition(int position){
        oldPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
        if(oldPosition != -1)
            notifyItemChanged(oldPosition);
    }

    public void setItems(List<PatternInfo> patterns){
        selectedPosition = oldPosition = -1;
        this.patterns = patterns;
        notifyDataSetChanged();
    }

    public void setSelectItem(String patternCode){
        int position = -1;
        for(int i = 0; i < patterns.size(); i++){
            if(patterns.get(i).getCode().equals(patternCode)){
                position = i;
                break;
            }
        }

        if(position == -1 || selectedPosition == position)
            return;

        setSelectedPosition(position);
    }
}
