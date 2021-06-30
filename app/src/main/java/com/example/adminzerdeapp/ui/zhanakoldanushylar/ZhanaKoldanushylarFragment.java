package com.example.adminzerdeapp.ui.zhanakoldanushylar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.adminzerdeapp.R;
import com.example.adminzerdeapp.adapters.SectionPagerAdapters;
import com.example.adminzerdeapp.modules.Users;
import com.example.adminzerdeapp.ui.zhanakoldanushylar.ConstantUsers.ConstantUsersFragment;
import com.example.adminzerdeapp.ui.zhanakoldanushylar.FirstRegistration.FirstRegistrationUsersFragment;
import com.example.adminzerdeapp.ui.zhanakoldanushylar.Satushylar.SatushylarFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ZhanaKoldanushylarFragment extends Fragment {
    View view;

    ViewPager viewPager;
    TabLayout tabLayout;
    DatabaseReference mDatabaseReference;
    Query query1, query2;
    int quantity1 = 0, quantity2 = 0;
    String strtext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhanakoldanushylar, container, false);

        setRetainInstance(true);

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout = view.findViewById(R.id.tabLayout);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        query1 = mDatabaseReference.orderByChild("ruqsat").equalTo("no");
        query2 = mDatabaseReference.orderByChild("ruqsat").equalTo("yes");

//        strtext = getArguments().getString("quantity");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void addFragments(ViewPager viewPager){
        SectionPagerAdapters adapters = new SectionPagerAdapters(getChildFragmentManager());

        adapters.addFragment(new FirstRegistrationUsersFragment(), "Жаңа қолданушылар");
        adapters.addFragment(new ConstantUsersFragment(), "Сатып алушылар");
        adapters.addFragment(new SatushylarFragment(), "Сатушылар");
        viewPager.setAdapter(adapters);
    }

    private void setUpViewPager(ViewPager viewPager) {
        addFragments(viewPager);
    }

}