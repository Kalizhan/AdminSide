package com.example.adminzerdeapp.ui.zhanakoldanushylar.ConstantUsers;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.adapters.ConstantUsersAdapter;
import com.example.adminzerdeapp.adapters.FirstUsersListAdapter;
import com.example.adminzerdeapp.modules.Satushylar;
import com.example.adminzerdeapp.modules.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConstantUsersFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Users> usersArrayList;
    ConstantUsersAdapter constantUsersAdapter;
    ProgressDialog progressDialog;
//    Button fab;

    EditText etNameSatushy, etSurnameSatushy, etEmailSatushy, etPasswordSatushy;
    Button btnAdd;

    DatabaseReference mDatabaseReference;
    FirebaseAuth mFirebaseAuth;
    Query query;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_constant_users, container, false);

        recyclerView = view.findViewById(R.id.recyclerConstant);
//        fab = view.findViewById(R.id.floatingBtn);
        usersArrayList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        query = mDatabaseReference.orderByChild("ruqsat").equalTo("yes");
        mFirebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");

        getData();

        constantUsersAdapter = new ConstantUsersAdapter(usersArrayList, getContext());
        recyclerView.setAdapter(constantUsersAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("floatBtn", "123 ");
//                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());
//                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_add, null);
//
//
//                etNameSatushy = dialogView.findViewById(R.id.etNameSatushy);
//                etSurnameSatushy = dialogView.findViewById(R.id.etSurnameSatushy);
//                etEmailSatushy = dialogView.findViewById(R.id.etEmailSatushy);
//                etPasswordSatushy = dialogView.findViewById(R.id.etPasswordSatushy);
//                btnAdd = dialogView.findViewById(R.id.btnAddSatushy);
//
//
//
//                btnAdd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
//                        progressDialog.setTitle("Мәліметтер енгізілуде...");
//                        progressDialog.setMessage("Күте тұрыңыз");
//                        progressDialog.show();
//
//                        if (etNameSatushy.getText().toString().isEmpty()){
//                            etNameSatushy.setError("Толық толтырыңыз!");
//                            return;
//                        }
//                        if (etSurnameSatushy.getText().toString().isEmpty()){
//                            etSurnameSatushy.setError("Толық толтырыңыз!");
//                            return;
//                        }
//                        if (etEmailSatushy.getText().toString().isEmpty()){
//                            etEmailSatushy.setError("Толық толтырыңыз!");
//                            return;
//                        }
//                        if (etPasswordSatushy.getText().toString().isEmpty()){
//                            etPasswordSatushy.setError("Толық толтырыңыз!");
//                            return;
//                        }
//                        if (etPasswordSatushy.length() <= 5){
//                            etPasswordSatushy.setError("Пароль 6 символдан аз болмауы керек!");
//                            return;
//                        }
//
//                        String etEmailSatushyreplace = etEmailSatushy.getText().toString().replace(".", "-");
//                        Satushylar satushylar = new Satushylar(etNameSatushy.getText().toString(), etSurnameSatushy.getText().toString(), etEmailSatushy.getText().toString(), etPasswordSatushy.getText().toString());
//                        mDatabaseReference.child("Satushylar").child(etEmailSatushyreplace).setValue(satushylar).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                mFirebaseAuth.createUserWithEmailAndPassword(etEmailSatushy.getText().toString(), etPasswordSatushy.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        progressDialog.cancel();
//                                        Toast.makeText(getActivity(), "Енгізілді!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        progressDialog.cancel();
//                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
//
//                dialog.setView(dialogView);
//                dialog.setCancelable(true);
//                dialog.show();
//            }
//        });
        return view;
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    private void getData() {
        progressDialog.show();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("gogo", "123 " + snapshot.getChildrenCount());
                    usersArrayList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Users users = snap.getValue(Users.class);
                        usersArrayList.add(users);
                        progressDialog.cancel();
                    }

                    constantUsersAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "Мәліметтер жоқ2", Toast.LENGTH_SHORT).show();
                    usersArrayList.clear();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}