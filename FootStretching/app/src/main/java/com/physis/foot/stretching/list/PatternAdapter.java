package com.physis.foot.stretching.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.helper.SwipeAndDragHelper;
import com.physis.foot.stretching.list.holder.DualLineHolder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PatternAdapter extends RecyclerView.Adapter<DualLineHolder> implements Filterable, SwipeAndDragHelper.OnActionCompletionContract {

    private ItemTouchHelper touchHelper;
    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public interface OnSelectedPatternListener{
        void onSelectedPattern(PatternInfo info);
        void onEditPattern(PatternInfo info);
    }

    private OnSelectedPatternListener listener;

    public void setOnSelectedPatternListener(OnSelectedPatternListener listener){
        this.listener = listener;
    }


    private List<PatternInfo> originalPatterns = new LinkedList<>();
    private List<PatternInfo> filteredPatterns = new LinkedList<>();

    private int selectedPosition = -1;
    private int oldPosition = -1;

    private boolean clickable = true;

    public void setItemClickable(boolean clickable){
        this.clickable = clickable;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                if(keyword.isEmpty()){
                    filteredPatterns = originalPatterns;
                }else{
                    List<PatternInfo> filteringPatterns = new LinkedList<>();
                    for(PatternInfo info : originalPatterns){
                        if(info.getKeyword().toLowerCase().contains(keyword.toLowerCase())){
                            filteringPatterns.add(info);
                        }
                    }
                    filteredPatterns = filteringPatterns;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredPatterns;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                filteredPatterns = (List<PatternInfo>) filterResults.values;
                selectedPosition = oldPosition = -1;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public DualLineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dual_line_text, parent, false);
        return new DualLineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DualLineHolder holder, final int position) {
        final PatternInfo info = filteredPatterns.get(position);

        holder.tvMainLine.setText(info.getName());
        holder.tvSubLine.setText(info.getKeyword().isEmpty() ? "NULL" : info.getKeyword());

        holder.itemFrame.setBackgroundResource(
                selectedPosition == position ? R.color.colorSelectedPatterns : R.color.colorPatterns);

        holder.itemFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null && clickable){
                    if(selectedPosition == position)
                        return;
                    setSelectedPosition(position);
                    listener.onSelectedPattern(info);
                }
            }
        });


        holder.itemFrame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(touchHelper != null && clickable)
                    touchHelper.startDrag(holder);
                return false;
            }
        });
//        holder.itemFrame.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if(listener != null)
//                    listener.onEditPattern(info);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return filteredPatterns.size();
    }

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        Collections.swap(originalPatterns, oldPosition, newPosition);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onViewSwiped(int position) {
        originalPatterns.remove(position);
        notifyItemRemoved(position);
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
        this.originalPatterns = filteredPatterns = patterns;
        notifyDataSetChanged();
    }

    public void setSelectItem(String patternCode){
        int position = -1;
        for(int i = 0; i < originalPatterns.size(); i++){
            if(originalPatterns.get(i).getCode().equals(patternCode)){
                position = i;
                break;
            }
        }

        if(position == -1 || selectedPosition == position)
            return;

        setSelectedPosition(position);
    }

    public List<PatternInfo> getItems(){
        return originalPatterns;
    }


}
