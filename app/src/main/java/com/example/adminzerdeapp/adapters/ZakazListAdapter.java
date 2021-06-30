package com.example.adminzerdeapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.modules.NewZakaz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ZakazListAdapter extends RecyclerView.Adapter<ZakazListAdapter.viewHolder> {
    Context context;
    ArrayList<NewZakaz> newZakazList;
    String[] codes, quantities, tovarName;

    public ZakazListAdapter(Context context, ArrayList<NewZakaz> newZakazList) {
        this.context = context;
        this.newZakazList = newZakazList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_zakaz_recycler, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        int pos = position + 1;
        NewZakaz newZakaz = newZakazList.get(position);

        holder.zakazOrder.setText("Тапсырыс " + pos);
        holder.enteredDate.setText(newZakaz.getDate());
        holder.zakazSituation.setText(newZakaz.getPaymentStyle());

        if (newZakaz.getPaymentStyle().equals("Толықтай төлеу")) {
            holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.yesil));
            holder.zakazAkshaTolenuKerek.setText(newZakaz.getTovarPrice() + " тг");
            holder.zakazAkshaKaldy.setText("0 тг");
        } else if (newZakaz.getPaymentStyle().equals("Жартылай төлеу")) {
            holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.sari));
            holder.zakazAkshaTolenuKerek.setText((int) (newZakaz.getTovarPrice() / 2) + " тг");
            holder.zakazAkshaKaldy.setText((int) (newZakaz.getTovarPrice() / 2) + " тг");
        } else if (newZakaz.getPaymentStyle().equals("Тауарды алған соң төлеу")) {
            holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.kyzyl));
            holder.zakazAkshaTolenuKerek.setText("0 тг");
            holder.zakazAkshaKaldy.setText(newZakaz.getTovarPrice() + " тг");
        }


        if (newZakaz.getTovarSituation().equals("new order")) {
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.neworder));
        } else if (newZakaz.getTovarSituation().equals("order accepted")) {
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.accept));
        } else if (newZakaz.getTovarSituation().equals("order got")) {
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.finished));
        }

        if (!newZakaz.getComment().isEmpty()) {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(newZakaz.getComment());
        } else {
            holder.comment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return newZakazList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView zakazOrder, enteredDate, zakazSituation, zakazAkshaTolenuKerek, zakazAkshaKaldy, comment;
        View zakazViewColor;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            zakazOrder = itemView.findViewById(R.id.tvzakaz);
            enteredDate = itemView.findViewById(R.id.tventereddate);
            zakazSituation = itemView.findViewById(R.id.tvZakazSituation);
            zakazAkshaTolenuKerek = itemView.findViewById(R.id.tvZakazPrice);
            zakazAkshaKaldy = itemView.findViewById(R.id.tvZakazKalganAksha);
            comment = itemView.findViewById(R.id.tvcomment);
            zakazViewColor = itemView.findViewById(R.id.view);
        }
    }
}
