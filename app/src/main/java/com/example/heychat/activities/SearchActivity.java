package com.example.heychat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.heychat.adapters.UsersAdapter;
import com.example.heychat.databinding.ActivitySearchBinding;
import com.example.heychat.listeners.UserListener;
import com.example.heychat.models.User;
import com.example.heychat.ultilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements UserListener {

    private ActivitySearchBinding binding;
    FirebaseFirestore database;
    private UsersAdapter usersAdapter;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users, this);
        binding.userRecyclerView.setAdapter(usersAdapter);
    }

    private void searchUser(String searchText) {
        loading(true);
        database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    if(task.isSuccessful() && task.getResult() != null){
                        users.clear();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if (queryDocumentSnapshot.getString(Constants.KEY_EMAIL).contains(searchText) && !searchText.isEmpty()){
                                User user = new User();
                                user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                                user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                                user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                                user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                user.id = queryDocumentSnapshot.getId();
                                users.add(user);
                            }
                        }
                        usersAdapter.notifyDataSetChanged();
                        if (users.size() <= 0){
                            showErrorMessage();
                        }
                    } else{
                        showErrorMessage();
                    }
                });
    }


    private void setListeners () {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.imageSearch.setOnClickListener(view -> searchUser(binding.edtSearch.getText().toString().trim()));
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(binding.edtSearch.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicker(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}