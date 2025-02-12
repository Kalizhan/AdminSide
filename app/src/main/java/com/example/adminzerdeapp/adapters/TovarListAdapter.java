package com.example.adminzerdeapp.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.TovarActivity;
import com.example.adminzerdeapp.modules.Tovar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TovarListAdapter extends RecyclerView.Adapter<TovarListAdapter.viewHolder> implements Filterable{
    List<Tovar> tovarList;
    List<Tovar> tovarListFull;
    Context context;
    DatabaseReference mDatabaseReference;

    public TovarListAdapter(List<Tovar> tovarList, Context context, DatabaseReference mDatabaseReference){
        this.tovarList = tovarList;
        this.context = context;
        this.mDatabaseReference = mDatabaseReference;
        tovarListFull = new ArrayList<>(tovarList);
    }

    @Override
    public Filter getFilter() {
        return FilterUser;
    }

    private Filter FilterUser = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            List<Tovar> filteredList = new ArrayList<>();
            if (constraint.length() == 0) {
                tovarListFull = tovarList;

            } else {
                for (Tovar row : tovarList) {

                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or phone number match
                    if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCode().contains(constraint)) {
                        filteredList.add(row);
                    }
                }

                tovarListFull = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = tovarListFull;
            //Log.i("constraint.length()", ""+constraint.length());
            return filterResults;
//            String searchText = constraint.toString().toLowerCase();
//            List<Tovar>templist = new ArrayList<>();
//            if (searchText.length() == 0 || searchText.isEmpty()){
//                templist.addAll(tovarListFull);
//            }else{
//                for (Tovar item:tovarListFull){
//                    if (item.getName().toLowerCase().contains(searchText) || item.getCode().toLowerCase().contains(searchText)){
//                        templist.add(item);
//                    }
//                }
//            }
//            FilterResults filterResults = new FilterResults();
//            filterResults.values = templist;
//            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tovarList = (ArrayList<Tovar>) results.values;
//            tovarList.clear();
//            tovarList.addAll((Collection<? extends Tovar>) results.values);
            notifyDataSetChanged();
        }
    };

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView photo, btnRemoveTovar;
        TextView name, price, code, quantity, availability;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imgGood);
            name = itemView.findViewById(R.id.titleOfMeal);
            price = itemView.findViewById(R.id.priceNum);
            code = itemView.findViewById(R.id.idNum);
            quantity = itemView.findViewById(R.id.quantity);
            availability = itemView.findViewById(R.id.inProcess);
            btnRemoveTovar = itemView.findViewById(R.id.btnRemoveTovar);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_tovar, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Tovar tovar = tovarList.get(position);

        Glide
                .with(context)
                .load(tovar.getPhoto())
                .centerCrop()
                .placeholder(R.drawable.box)
                .into(holder.photo);

        holder.name.setText(tovar.getName());
        holder.price.setText(String.valueOf(tovar.getPrice()) + " тг/дана");
        holder.code.setText("#"+tovar.getCode());
        holder.quantity.setText(tovar.getQuantity()+" дана");

        if (tovar.getQuantity()==0){
            holder.availability.setVisibility(View.VISIBLE);
        }else{
            holder.availability.setVisibility(View.INVISIBLE);
        }

//        if (Integer.parseInt(String.valueOf(holder.quantity.getText())) <= 10 && Integer.parseInt(String.valueOf(holder.quantity.getText())) > 0){
//            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.yellow));
//        }else if (Integer.parseInt(String.valueOf(holder.quantity.getText())) == 0){
//            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.red));
//        }else{
//            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
//        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });

        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });

        holder.code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });

        holder.btnRemoveTovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogRepeat = new Dialog(context);
                dialogRepeat.show();
                dialogRepeat.setContentView(R.layout.custom_dialog_repeat_zakaz);
                dialogRepeat.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialogRepeat.setCancelable(true);

                Button okey = dialogRepeat.findViewById(R.id.btnYes);

                okey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "YEEEEEEEEES", Toast.LENGTH_SHORT).show();
                        mDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                        Tovar tovar = snapshot1.getValue(Tovar.class);
                                        if (holder.code.getText().toString().equals(tovar.getCode())){
                                            snapshot1.getRef().removeValue();
                                            notifyItemRemoved(position);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void goToNextPage(int position) {
        Intent intent = new Intent(context, TovarActivity.class);

        intent.putExtra("tovar", tovarList.get(position));

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return tovarList.size();
    }
}
