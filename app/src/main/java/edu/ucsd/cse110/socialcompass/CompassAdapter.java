package edu.ucsd.cse110.socialcompass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CompassAdapter extends RecyclerView.Adapter<CompassAdapter.ViewHolder> {
    public List<Friend> friendsList = Collections.emptyList();

    public void setFriendsList(List<Friend> newFriendsList) {
        this.friendsList.clear();
        this.friendsList = newFriendsList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.friend, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setFriend(friendsList.get(position));
    }

    @Override
    public int getItemCount() { return friendsList.size(); }

    @Override
    public long getItemId(int position) { return friendsList.get(position).id; }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private Friend friend;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);

        }


        public Friend getFriend() { return friend; }

        public void setFriend(Friend friend) {
            this.friend = friend;
            this.name.setText(friend.name);
            var friendParams = this.name.getLayoutParams();

            //set friend grid row col

        }


    }
}
