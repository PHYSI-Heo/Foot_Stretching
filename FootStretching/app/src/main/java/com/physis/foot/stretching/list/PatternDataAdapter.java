package com.physis.foot.stretching.list;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.list.holder.DualLineHolder;
import com.physis.foot.stretching.list.holder.PatternDataHolder;

import java.util.LinkedList;
import java.util.List;

public class PatternDataAdapter extends RecyclerView.Adapter<PatternDataHolder> {

    private List<PatternInfo> itemList = new LinkedList<>();

    @NonNull
    @Override
    public PatternDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pattern_data, parent, false);
        return new PatternDataHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatternDataHolder holder, int position) {
        PatternInfo info = itemList.get(position);

        holder.tvName.setText(info.getName());
        holder.tvSubData.setText("( " + info.getMaker() + " / " + info.getRuntime() + " Sec )");
        holder.tvExplanation.setText(info.getExplanation());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
