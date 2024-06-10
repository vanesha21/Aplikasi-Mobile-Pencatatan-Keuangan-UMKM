package com.example.project_rajut;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

public class Catatanpemasukan extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    viewAdapter viewadapter;

    ImageView exit;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatanpemasukan);

        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.viewpager);
        exit = findViewById(R.id.out);
        viewadapter = new viewAdapter(this);

        viewPager2.setAdapter(viewadapter);

        // BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        // badgeDrawable.setNumber(20);
        // badgeDrawable.setMaxCharacterCount(2);
        // badgeDrawable.setBackgroundColor(Color.RED);
        // badgeDrawable.setVisible(true);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }
}