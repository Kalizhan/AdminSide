package com.example.adminzerdeapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.modules.Users;
import com.example.adminzerdeapp.ui.zhanakoldanushylar.FirstRegistration.FirstRegistrationUsersFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirstUsersListAdapter extends RecyclerView.Adapter<FirstUsersListAdapter.viewHolder> {
    List<Users> usersList;
    Context context;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    public FirstUsersListAdapter(List<Users> usersList, Context context, DatabaseReference databaseReference) {
        this.usersList = usersList;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_new_users, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users users = usersList.get(position);

        holder.name.setText(users.getFullName());
        holder.email.setText(users.getEmail());
        holder.phone.setText(users.getPhone());
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            mAuth = FirebaseAuth.getInstance();
                            Users users1 = snap.getValue(Users.class);
                            //Log.i("userEmail", "123456 "+ users1.getEmail());
                            if (holder.email.getText().toString().equals(users1.getEmail())) {
                                String userEmail = users1.getEmail().replace(".", "-");
                                //Log.i("userEmail", "123456 "+ userEmail);
                                databaseReference.child(userEmail).child("ruqsat").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mAuth.createUserWithEmailAndPassword(users.getEmail(), users.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()){
                                                    databaseReference.child(userEmail).child("userId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Toast.makeText(context, "Енгізілді", Toast.LENGTH_SHORT).show();
                                                                    notifyItemChanged(position);
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if (e instanceof FirebaseAuthUserCollisionException){
                                                    Toast.makeText(context, "Email қолданыста!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
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

        holder.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                            Users users2 = appleSnapshot.getValue(Users.class);
                            if (holder.email.getText().toString().equals(users2.getEmail())) {
                                appleSnapshot.getRef().removeValue();
                                notifyItemRemoved(position);
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

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView name, email, phone;
        ImageView btnAdd, btnCross;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvNameOfUsers);
            email = itemView.findViewById(R.id.tvEmailofUsers);
            phone = itemView.findViewById(R.id.tvPhoneofUsers);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnCross = itemView.findViewById(R.id.btnCross);
        }
    }
}
