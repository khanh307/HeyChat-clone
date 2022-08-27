package com.example.heychat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.heychat.R;
import com.example.heychat.adapters.RecentConversationAdapter;
import com.example.heychat.databinding.ActivityMain2Binding;
import com.example.heychat.fragments.ConfigFragment;
import com.example.heychat.fragments.HomeFragment;
import com.example.heychat.fragments.ProfileFragment;
import com.example.heychat.fragments.VideoCallFragment;
import com.example.heychat.models.ChatMessage;
import com.example.heychat.ultilities.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.List;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private ActivityMain2Binding binding;
    private TextView item1, item2, item3, item4, select;
    private ColorStateList def;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        moveToFragement(new HomeFragment());

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item4 = findViewById(R.id.item4);

        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        item4.setOnClickListener(this);

        select = findViewById(R.id.select);
        def = item2.getTextColors();

    }



    private void moveToFragement(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.item1) {
            collapsingToolbarLayout.setTitle("Chat with Friends");
            select.animate().x(0).setDuration(100);
            item1.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
            item2.setTextColor(def);
            item3.setTextColor(def);
            item4.setTextColor(def);
            moveToFragement(new HomeFragment());
        } else if (v.getId() == R.id.item2) {
            collapsingToolbarLayout.setTitle("Call with Friends");
            item1.setTextColor(def);
            item2.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
            item3.setTextColor(def);
            item4.setTextColor(def);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            moveToFragement(new VideoCallFragment());
        } else if (v.getId() == R.id.item3) {
            collapsingToolbarLayout.setTitle("Contacts");
            item1.setTextColor(def);
            item2.setTextColor(def);
            item3.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
            item4.setTextColor(def);
            int size = item2.getWidth() * 2;
            select.animate().x(size).setDuration(100);
            moveToFragement(new ConfigFragment());
        } else if (v.getId() == R.id.item4) {
            collapsingToolbarLayout.setTitle("Profile");
            item1.setTextColor(def);
            item2.setTextColor(def);
            item3.setTextColor(def);
            item4.setTextColor(ContextCompat.getColorStateList(this, R.color.colorWhile));
            int size = item2.getWidth() * 3;
            select.animate().x(size).setDuration(100);
            moveToFragement(new ProfileFragment());
        }
    }
}