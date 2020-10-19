package com.physis.foot.stretching.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.physis.foot.stretching.R;
import com.physis.foot.stretching.data.PatternInfo;
import com.physis.foot.stretching.data.UserInfo;
import com.physis.foot.stretching.list.holder.UserHolder;

import java.util.LinkedList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> implements Filterable {

    public interface OnSelectedUserListener{
        void onSelectUser(UserInfo info);
    }

    private OnSelectedUserListener listener;

    public void setOnSelectedUserListener(OnSelectedUserListener listener){
        this.listener = listener;
    }

    private List<UserInfo> originalUsers = new LinkedList<>();
    private List<UserInfo> filteredUsers = new LinkedList<>();

    private int selectedPosition = -1;
    private int oldPosition = -1;

    private int[] userColors = new int[]{
            R.color.colorUser1,  R.color.colorUser2,  R.color.colorUser3,  R.color.colorUser4,
    };

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String name = charSequence.toString();
                if(name.isEmpty()){
                    filteredUsers = originalUsers;
                }else{
                    List<UserInfo> filteringUsers = new LinkedList<>();
                    for(UserInfo info : originalUsers){
                        if(info.getName().toLowerCase().contains(name.toLowerCase())){
                            filteringUsers.add(info);
                        }
                    }
                    filteredUsers = filteringUsers;
                }
                FilterResults results = new FilterResults();
                results.values = filteredUsers;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                filteredUsers = (List<UserInfo>) filterResults.values;
                selectedPosition = oldPosition = -1;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {
        final UserInfo info = filteredUsers.get(position);

        holder.tvName.setText(info.getName());
        holder.tvPhone.setText(info.getPhone());

        holder.ivPersonIcon.setBackgroundResource(userColors[position % userColors.length]);
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
        return filteredUsers.size();
    }

    public void setItems(List<UserInfo> userInfos){
        originalUsers = filteredUsers = userInfos;
        notifyDataSetChanged();
    }
}
