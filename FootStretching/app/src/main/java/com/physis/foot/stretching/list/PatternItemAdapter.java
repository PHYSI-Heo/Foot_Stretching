package com.physis.foot.stretching.list;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternItemInfo;
import com.physis.foot.stretching.helper.SwipeAndDragHelper;
import com.physis.foot.stretching.helper.SystemEnv;
import com.physis.foot.stretching.list.holder.PatternItemHolder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PatternItemAdapter extends RecyclerView.Adapter<PatternItemHolder> implements SwipeAndDragHelper.OnActionCompletionContract {

    public interface OnSelectedPatternItemListener{
        void onPatternItemClick(PatternItemInfo info);
    }

    private ItemTouchHelper touchHelper;
    private OnSelectedPatternItemListener selectedPatternItemListener;

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }

    public void setSelectedPatternItemListener(OnSelectedPatternItemListener listener){
        this.selectedPatternItemListener = listener;
    }

    private List<PatternItemInfo> itemInfos = new LinkedList<>();
    private int selectedPosition = -1;
    private int oldPosition = -1;

    @NonNull
    @Override
    public PatternItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pattern_info, parent, false);
        return new PatternItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final PatternItemHolder holder, final int position) {
        final PatternItemInfo info = itemInfos.get(position);
        holder.tvOrder.setText(String.valueOf(info.getOrder()));
        String[] leftDatas = info.getLeftMoving().split(",");
        String[] rightDatas = info.getRightMoving().split(",");

        holder.tvLeftDirectionAngle.setText(getDirectionText(leftDatas[0]) + "\n" + SystemEnv.getAnglePhaseValue(leftDatas[0], leftDatas[1]) + "");
        holder.tvLeftMovingSpeed.setText("Moving\n" + leftDatas[3] + " Phase");
        holder.tvLeftHoldingTime.setText("Holding\n" + leftDatas[4] + " Sec");
        holder.tvLeftReturnable.setTextColor(leftDatas[2].equals("1") ? Color.GREEN : Color.GRAY);

        holder.tvRightDirectionAngle.setText(getDirectionText(rightDatas[0]) + "\n" + SystemEnv.getAnglePhaseValue(rightDatas[0], rightDatas[1]) + "");
        holder.tvRightMovingSpeed.setText("Moving\n" + rightDatas[3] + " Phase");
        holder.tvRightHoldingTime.setText("Holding\n" + rightDatas[4] + " Sec");
        holder.tvRightReturnable.setTextColor(rightDatas[2].equals("1") ? Color.GREEN : Color.GRAY);

        holder.layoutItem.setBackgroundResource(
                selectedPosition == position ? R.color.colorSelectedPatterns : R.color.colorPatterns);

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
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

                if(selectedPatternItemListener != null)
                    selectedPatternItemListener.onPatternItemClick(selectedPosition == -1 ? null : info);
            }
        });

        holder.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                touchHelper.startDrag(holder);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemInfos.size();
    }

    private String getDirectionText(String key){
        switch (key){
            case "1":
                return "UP";
            case "2":
                return "DOWN";
            case "3":
                return "LEFT";
            default:
                return "RIGHT";
        }
    }

    public void setItems(List<PatternItemInfo> itemInfos){
        this.itemInfos = itemInfos;
        notifyDataSetChanged();
    }

    public void clearItem(){
        this.itemInfos.clear();
        notifyDataSetChanged();
    }

    public List<PatternItemInfo> getItems(){
        return itemInfos;
    }

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        Collections.swap(itemInfos, oldPosition, newPosition);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onViewSwiped(int position) {
        itemInfos.remove(position);
        notifyItemRemoved(position);
    }

    public void renewSelectItem() {
        notifyItemChanged(selectedPosition);
    }
}
