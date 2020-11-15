package com.physis.foot.stretching.list;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.list.holder.PatternDataHolder;

import java.util.LinkedList;
import java.util.List;

public class PatternDataAdapter extends RecyclerView.Adapter<PatternDataHolder> implements Filterable {

    private static final String TAG = PatternDataAdapter.class.getSimpleName();

    public static final int PATTERN_DETAIL = 1;
    public static final int PATTERN_ADD_ITEM = 2;


    public void setItems(List<PatternInfo> itemList) {
        this.originalItems = filteredItems = itemList;
        notifyDataSetChanged();
    }

    public interface OnSelectedPatternDataListener{
        void onSelected(int type, PatternInfo info);
    }

    private OnSelectedPatternDataListener listener;

    public void setOnSelectedPatternDataListener(OnSelectedPatternDataListener listener){
        this.listener = listener;
    }


    private List<PatternInfo> originalItems = new LinkedList<>();
    private List<PatternInfo> filteredItems = new LinkedList<>();

    private int selectedPosition = -1;
    private int oldPosition = -1;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                Log.e(TAG, "Input Keyword : " + keyword);
                if(keyword.isEmpty()){
                    filteredItems = originalItems;
                }else{
                    List<PatternInfo> filteringPatterns = new LinkedList<>();
                    for(PatternInfo info : originalItems){
                        if(info.getKeyword().toLowerCase().contains(keyword.toLowerCase())){
                            Log.e(TAG, "Filtering : " + info.getKeyword());
                            filteringPatterns.add(info);
                        }
                    }
                    filteredItems = filteringPatterns;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                filteredItems = (List<PatternInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public PatternDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pattern_data, parent, false);
        return new PatternDataHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatternDataHolder holder, final int position) {
        final PatternInfo info = originalItems.get(position);

        holder.tvName.setText(info.getName());
        holder.tvSubData.setText("( HOSPITAL : " + info.getMaker() + " / RUNTIME : " + info.getRuntime() + " Sec )");
        holder.tvExplanation.setText(info.getExplanation());

        holder.itemLayout.setBackgroundResource(
                selectedPosition == position ? R.color.colorSelectedPatterns : R.color.colorPatterns);

        holder.ivBtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition == position)
                    return;
                setSelectedPosition(position);

                if(listener != null)
                    listener.onSelected(PATTERN_DETAIL, info);
            }
        });

        holder.ivBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onSelected(PATTERN_ADD_ITEM, info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return originalItems.size();
    }

    private void setSelectedPosition(int position){
        oldPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
        if(oldPosition != -1)
            notifyItemChanged(oldPosition);
    }


}
