package com.example.adminzerdeapp.ui.zhanakoldanushylar.Satushylar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.adapters.ConstantUsersAdapter;
import com.example.adminzerdeapp.adapters.SatushylarAdapter;
import com.example.adminzerdeapp.modules.Satushylar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SatushylarFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<Satushylar> satushylarArrayList;
    SatushylarAdapter satushylarAdapter;
    ProgressDialog progressDialog;
    Button fab;

    EditText etNameSatushy, etSurnameSatushy, etEmailSatushy, etPasswordSatushy;
    MaskEditText etPhoneNumberSatushy;
    Button btnAdd;

    DatabaseReference mDatabaseReference;
    FirebaseAuth mFirebaseAuth;

    private static final String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_satushylar, container, false);

        recyclerView = view.findViewById(R.id.recyclerSatushylar);
        fab = view.findViewById(R.id.floatingBtn);
        satushylarArrayList = new ArrayList<Satushylar>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Satushylar");
        mFirebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");

        getData();

        satushylarAdapter = new SatushylarAdapter(satushylarArrayList, getContext());
        recyclerView.setAdapter(satushylarAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("floatBtn", "123 ");
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_add, null);


                etNameSatushy = dialogView.findViewById(R.id.etNameSatushy);
                etSurnameSatushy = dialogView.findViewById(R.id.etSurnameSatushy);
                etEmailSatushy = dialogView.findViewById(R.id.etEmailSatushy);
                etPasswordSatushy = dialogView.findViewById(R.id.etPasswordSatushy);
                etPhoneNumberSatushy = dialogView.findViewById(R.id.etPhoneNumberSatushy);
                btnAdd = dialogView.findViewById(R.id.btnAddSatushy);



                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etNameSatushy.getText().toString().isEmpty()){
                            etNameSatushy.setError("Толық толтырыңыз!");
                            return;
                        }
                        if (etSurnameSatushy.getText().toString().isEmpty()){
                            etSurnameSatushy.setError("Толық толтырыңыз!");
                            return;
                        }
                        if (etPhoneNumberSatushy.getText().toString().isEmpty()){
                            etPhoneNumberSatushy.setError("Толық толтырыңыз!");
                            return;
                        }
                        if (etEmailSatushy.getText().toString().isEmpty()){
                            etEmailSatushy.setError("Толық толтырыңыз!");
                            return;
                        }
                        if (etPasswordSatushy.getText().toString().isEmpty()){
                            etPasswordSatushy.setError("Толық толтырыңыз!");
                            return;
                        }
                        if (etPasswordSatushy.length() <= 5){
                            etPasswordSatushy.setError("Пароль 6 символдан аз болмауы керек!");
                            return;
                        }
                        if (etPhoneNumberSatushy.length() <= 9){
                            etPhoneNumberSatushy.setError("Телефон номері толық емес!");
                            return;
                        }

                        Pattern pattern = Pattern.compile(regex);

                        Matcher matcher = pattern.matcher(etEmailSatushy.getText().toString());

                        //Toast.makeText(getActivity(), "The Email address " + etEmailSatushy.getText().toString() + " is " + (matcher.matches() ? "valid" : "invalid"), Toast.LENGTH_SHORT).show();

                        if ((matcher.matches() ? "valid" : "invalid").equals("invalid")){
                            etEmailSatushy.setError("Email дұрыс жазыңыз");
                            return;
                        }

                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setTitle("Мәліметтер енгізілуде...");
                        progressDialog.setMessage("Күте тұрыңыз");
                        progressDialog.show();

                        String etEmailSatushyreplace = etEmailSatushy.getText().toString().replace(".", "-");
                        Satushylar satushylar = new Satushylar(etNameSatushy.getText().toString(), etSurnameSatushy.getText().toString(), etEmailSatushy.getText().toString(), etPasswordSatushy.getText().toString(), etPhoneNumberSatushy.getText().toString());
                        mFirebaseAuth.fetchSignInMethodsForEmail(etEmailSatushy.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean check = !task.getResult().getSignInMethods().isEmpty();

                                if (!check){
                                    mFirebaseAuth.createUserWithEmailAndPassword(etEmailSatushy.getText().toString(), etPasswordSatushy.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressDialog.cancel();
                                            if (task.isSuccessful()){
                                                mDatabaseReference.child(etEmailSatushyreplace).setValue(satushylar).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getActivity(), "Енгізілді!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.cancel();
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    progressDialog.cancel();
                                    Toast.makeText(getContext(), "Email тіркелген!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });

                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
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

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    satushylarArrayList.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Satushylar satushylar = snap.getValue(Satushylar.class);
                        satushylarArrayList.add(satushylar);
                        progressDialog.cancel();
                    }

                    satushylarAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "Мәліметтер жоқ3", Toast.LENGTH_SHORT).show();
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