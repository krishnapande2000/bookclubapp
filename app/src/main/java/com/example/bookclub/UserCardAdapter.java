package com.example.bookclub;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserHolder> {

    private Context context;
    private ArrayList<User> users;

    public UserCardAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_display_card, parent, false);
        return new UserHolder(view);


    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull UserCardAdapter.UserHolder holder, int position) {
        User user = users.get(position);
        holder.setDetails(user);
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtPhone,txtAddress;

        UserHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAddress = itemView.findViewById(R.id.txtAddress);

        }

        void setDetails(User user) {
            txtName.setText(user.getName());
            txtPhone.setText(user.getPhone());
            txtAddress.setText(user.getAddress());

        }
    }
}