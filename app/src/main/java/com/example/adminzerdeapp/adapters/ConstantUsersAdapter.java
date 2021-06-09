package com.example.adminzerdeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.modules.Users;

import java.util.List;

public class ConstantUsersAdapter extends RecyclerView.Adapter<ConstantUsersAdapter.viewHolder>{
    List<Users> usersList;
    Context context;

    public ConstantUsersAdapter(List<Users> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_constant_users, parent, false);
        return new ConstantUsersAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = usersList.get(position);

        holder.name.setText(users.getFullName());
        holder.email.setText(users.getEmail());
        holder.phone.setText(users.getPhone());
        holder.linearLayoutPassword.setVisibility(View.GONE);
        holder.shareClick.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone;
        LinearLayout linearLayoutPassword;
        ImageView shareClick;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNameOfUsers);
            email = itemView.findViewById(R.id.tvEmailofUsers);
            phone = itemView.findViewById(R.id.tvPhoneofUsers);
            linearLayoutPassword = itemView.findViewById(R.id.linearLayoutPassword);
            shareClick = itemView.findViewById(R.id.shareClick);
        }
    }
}
