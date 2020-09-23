package com.physis.foot.stretching.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.UserInfo;
import com.physis.foot.stretching.list.holder.UserHolder;

import java.util.LinkedList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    public interface OnSelectedUserListener{
        void onSelectUser(UserInfo info);
    }

    private OnSelectedUserListener listener;

    public void setOnSelectedUserListener(OnSelectedUserListener listener){
        this.listener = listener;
    }

    private List<UserInfo> users = new LinkedList<>();
    private int selectedPosition = -1;
    private int oldPosition = -1;

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {
        final UserInfo info = users.get(position);

        holder.tvName.setText(info.getName());
        holder.tvPhone.setText(info.getPhone());

        holder.itemFrame.setBackgroundResource(
                selectedPosition == position ? R.color.colorSelectedPatterns : R.color.colorPatterns);

        holder.itemFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition == position)
                    return;

                oldPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
                if(oldPosition != -1)
                    notifyItemChanged(oldPosition);

                if(listener != null)
                    listener.onSelectUser(info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setItems(List<UserInfo> userInfos){
        users = userInfos;
        notifyDataSetChanged();
    }
}
