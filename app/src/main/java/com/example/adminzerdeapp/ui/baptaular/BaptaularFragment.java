package com.example.adminzerdeapp.ui.baptaular;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.TapsyrystarActivity;

public class BaptaularFragment extends Fragment {
    View v;
    TextView projectAbout, tapsyrystarKoru;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_baptaular, container, false);

        tapsyrystarKoru = v.findViewById(R.id.btnTapsyrystardyKoru);
        projectAbout = v.findViewById(R.id.btnAboutProject);

        tapsyrystarKoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TapsyrystarActivity.class);

                startActivity(intent);
            }
        });
        return v;
    }
}