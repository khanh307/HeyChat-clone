package com.example.heychat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.heychat.R;
import com.example.heychat.adapters.RecentConversationAdapter;
import com.example.heychat.databinding.ActivityMain2Binding;
import com.example.heychat.firebase.ViewPagerAdapter;
import com.example.heychat.fragments.ConfigFragment;
import com.example.heychat.fragments.HomeFragment;
import com.example.heychat.fragments.ProfileFragment;
import com.example.heychat.fragments.VideoCallFragment;
import com.example.heychat.models.ChatMessage;
import com.example.heychat.ultilities.PreferenceManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.List;

public class MainActivity2 extends AppCompatActivity{

    private ActivityMain2Binding binding;
//    private TextView item1, item2, item3, item4, select;
//    private ColorStateList def;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        setUpTablayout();



//        item1 = findViewById(R.id.item1);
//        item2 = findViewById(R.id.item2);
//        item3 = findViewById(R.id.item3);
//        item4 = findViewById(R.id.item4);
//
//        item1.setOnClickListener(this);
//        item2.setOnClickListener(this);
//        item3.setOnClickListener(this);
//        item4.setOnClickListener(this);
//
//        select = findViewById(R.id.select);
//        def = item2.getTextColors();

    }

    private void setUpTablayout() {
        viewPagerAdapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(binding.tablayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Chat");
                        collapsingToolbarLayout.setTitle("Chat with friends");
                        break;
                    case 1:
                        tab.setText("Call");
                        collapsingToolbarLayout.setTitle("Call with friends");
                        break;
                    case 2:
                        tab.setText("Contacts");
                        collapsingToolbarLayout.setTitle("Contacts");
                        break;
                    case 3:
                        tab.setText("Profile");
                        collapsingToolbarLayout.setTitle("Profile");
                        break;
                }
            }
        }).attach();

        for(int i = 0; i < 4; i++){
            TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_title, null, false);
            binding.tablayout.getTabAt(i).setCustomView(textView);

        }
    }


//    private void moveToFragement(Fragment fragment) {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .commit();
//    }

//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.item1) {
//            collapsingToolbarLayout.setTitle("Chat with Friends");
//            select.animate().x(0).setDuration(100);
//            item1.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
//            item2.setTextColor(def);
//            item3.setTextColor(def);
//            item4.setTextColor(def);
//            moveToFragement(new HomeFragment());
//        } else if (v.getId() == R.id.item2) {
//            collapsingToolbarLayout.setTitle("Call with Friends");
//            item1.setTextColor(def);
//            item2.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
//            item3.setTextColor(def);
//            item4.setTextColor(def);
//            int size = item2.getWidth();
//            select.animate().x(size).setDuration(100);
//            moveToFragement(new VideoCallFragment());
//        } else if (v.getId() == R.id.item3) {
//            collapsingToolbarLayout.setTitle("Contacts");
//            item1.setTextColor(def);
//            item2.setTextColor(def);
//            item3.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
//            item4.setTextColor(def);
//            int size = item2.getWidth() * 2;
//            select.animate().x(size).setDuration(100);
//            moveToFragement(new ConfigFragment());
//        } else if (v.getId() == R.id.item4) {
//            collapsingToolbarLayout.setTitle("Profile");
//            item1.setTextColor(def);
//            item2.setTextColor(def);
//            item3.setTextColor(def);
//            item4.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
//            int size = item2.getWidth() * 3;
//            select.animate().x(size).setDuration(100);
//            moveToFragement(new ProfileFragment());
//        }
//    }
}