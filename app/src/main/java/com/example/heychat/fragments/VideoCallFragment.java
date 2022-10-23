package com.example.heychat.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heychat.R;
import com.example.heychat.activities.OutgoingInvitationActivity;
import com.example.heychat.adapters.CallAdapter;
import com.example.heychat.adapters.UsersAdapter;
import com.example.heychat.listeners.CallListener;
import com.example.heychat.listeners.UserListener;
import com.example.heychat.models.User;
import com.example.heychat.ultilities.Constants;
import com.example.heychat.ultilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;
import java.util.List;


public class VideoCallFragment extends Fragment implements CallListener{

    private PreferenceManager preferenceManager;
    private RecyclerView recyclerView;
    private CallAdapter callAdapter;
    private ArrayList<User> mUsers;
    private ProgressBar progressBar;
    private final ArrayList<String> contactList = new ArrayList<>();


    public VideoCallFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_call, container, false);
        recyclerView = view.findViewById(R.id.videocall_recyclerview);
        progressBar = view.findViewById(R.id.call_progressbar);
        preferenceManager = new PreferenceManager(getContext());

        mUsers = new ArrayList<>();
        callAdapter = new CallAdapter(mUsers, new UserListener() {
            @Override
            public void onUserClicker(User user) {

            }
        }, this);
        recyclerView.setAdapter(callAdapter);

        requestPermission();
        return view;
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            progressBar.setVisibility(View.VISIBLE);
        } else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void getUsers(){
        loading(true);
        getContactList();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null){

                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            for (int i = 0; i < contactList.size(); i++){
                                System.out.println(contactList.get(i));
                                if (contactList.get(i).equals(queryDocumentSnapshot.getString(Constants.KEY_EMAIL))){
                                    User user = new User();
                                    user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                                    user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                                    user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                                    user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                                    user.id = queryDocumentSnapshot.getId();
                                    mUsers.add(user);
                                }
                            }
                        }
                        callAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void requestPermission(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getUsers();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .check();
    }


    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        try (Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null)) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                    try (Cursor phoneCursor = getActivity().getContentResolver().query(uriPhone, null, selection, new String[]{id}, null)) {

                        if (phoneCursor.moveToNext()) {
                            @SuppressLint("Range") String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            number = number.replaceAll("\\s+","");
                            contactList.add(number.trim());
                        }
                    }
                }
            }
        }

    }

    @Override
    public void initiateVideoCall(User user) {
        if(user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(getContext(), user.name +"is not available for video call", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "video");
            startActivity(intent);
        }
    }

    @Override
    public void initiateAudioCall(User user) {

        if(user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(getContext(), user.name +"is not available for audio call", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "audio");
            startActivity(intent);


        }
    }
}