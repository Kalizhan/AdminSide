package com.example.adminzerdeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adminzerdeapp.adapters.ZakazListAdapter;
import com.example.adminzerdeapp.modules.NewZakaz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TapsyrystarActivity extends AppCompatActivity {
    LinearLayout tapsyrystarZhok;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<NewZakaz> newZakazArrayList;
    ZakazListAdapter zakazListAdapter;

    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapsyrystar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        tapsyrystarZhok = findViewById(R.id.TapsyrystarZhok);

        recyclerView = findViewById(R.id.recyclerTapsyrys);
        newZakazArrayList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Zakazdar");
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(TapsyrystarActivity.this);
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");

        getData();

        zakazListAdapter = new ZakazListAdapter(TapsyrystarActivity.this, newZakazArrayList);
        recyclerView.setAdapter(zakazListAdapter);
        linearLayoutManager = new LinearLayoutManager(TapsyrystarActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {
                        Intent intent = new Intent(getApplicationContext(), ZakazWindowActivity.class);
                        intent.putExtra("zakaz", newZakazArrayList.get(pos));

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        new CountDownTimer(5000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (newZakazArrayList.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Байланысты тексеріңіз", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        progressDialog.show();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newZakazArrayList.clear();
                if (snapshot.exists()) {
                    tapsyrystarZhok.setVisibility(View.GONE);
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        for (DataSnapshot snap1 : snap.getChildren()){
                            NewZakaz newZakaz = snap1.getValue(NewZakaz.class);

                            newZakazArrayList.add(newZakaz);
                            progressDialog.cancel();
                        }
                    }
                    zakazListAdapter.notifyDataSetChanged();
                } else {
                    newZakazArrayList.clear();
                    tapsyrystarZhok.setVisibility(View.VISIBLE);
                    progressDialog.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}