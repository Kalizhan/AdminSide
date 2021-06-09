package com.example.adminzerdeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.modules.Satushylar;

import java.util.List;

public class SatushylarAdapter extends RecyclerView.Adapter<SatushylarAdapter.viewHolder> {
    List<Satushylar> satushylarList;
    Context context;

    public SatushylarAdapter(List<Satushylar> satushylarList, Context context) {
        this.satushylarList = satushylarList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_constant_users, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Satushylar satushylar = satushylarList.get(position);

        holder.name.setText(satushylar.getNameSatushy() + " " + satushylar.getSurnameSatushy());
        holder.email.setText(satushylar.getEmailSatushy());
        holder.phone.setText(satushylar.getPhoneNumberSatushy());
        holder.linearLayoutPassword.setVisibility(View.VISIBLE);
        holder.shareClick.setVisibility(View.VISIBLE);
        holder.password.setText(satushylar.getPasswordSatushy());

        holder.shareClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                whatsappIntent.setType("text/plain");
//
//                whatsappIntent.setPackage("com.whatsapp");
//                whatsappIntent.putExtra(Intent.EXTRA_TEXT, satushylar.getEmailSatushy() +" "+ satushylar.getPasswordSatushy());
//                whatsappIntent.putExtra(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+"+7"+satushylar.getPhoneNumberSatushy()));
//                try {
//                    context.startActivity(whatsappIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(context, "Whatsapp орнатыңыз", Toast.LENGTH_SHORT).show();
//                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
//                }
//                if (appInstalledOrNot()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone="+ satushylar.getPhoneNumberSatushy() +
                            "&text=Zerde App!\nСіздің емайл: " + satushylar.getEmailSatushy() +"\nСіздің пароль: "+ satushylar.getPasswordSatushy()));
                    context.startActivity(intent);
//                }
//                else {
//                    Toast.makeText(context, "Whatsapp орнатыңыз", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

//    private boolean appInstalledOrNot(){
//        PackageManager packageManager = context.getPackageManager();
//        boolean app_installed;
//        try{
//            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//            app_installed = true;
//        }catch (PackageManager.NameNotFoundException e){
//            app_installed = false;
//        }
//        return app_installed;
//    }

    @Override
    public int getItemCount() {
        return satushylarList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone, password;
        ImageView shareClick;
        LinearLayout linearLayoutPassword;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNameOfUsers);
            email = itemView.findViewById(R.id.tvEmailofUsers);
            phone = itemView.findViewById(R.id.tvPhoneofUsers);
            password = itemView.findViewById(R.id.tvPasswordofUsers);
            shareClick = itemView.findViewById(R.id.shareClick);
            linearLayoutPassword = itemView.findViewById(R.id.linearLayoutPassword);
        }
    }
}