package com.example.heychat.adapters;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telecom.Call;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heychat.R;
import com.example.heychat.databinding.ItemContainerUserBinding;
import com.example.heychat.fragments.VideoCallFragment;
import com.example.heychat.listeners.CallListener;
import com.example.heychat.listeners.UserListener;
import com.example.heychat.models.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {

    private final List<User> users;
    private final UserListener userListener;
    private final CallListener callListener;

    public CallAdapter(List<User> users, UserListener userListener, CallListener callListener) {
        this.users = users;
        this.userListener = userListener;
        this.callListener = callListener;
    }



    class CallViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        CircleImageView image_user;
        ImageView video_call_btn;
        ImageView audio_call_btn;


        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_name_call);
            image_user = itemView.findViewById(R.id.image_user);
            video_call_btn = itemView.findViewById(R.id.video_call_btn);
            audio_call_btn = itemView.findViewById(R.id.audio_call_btn);
        }

        void setUserData(User user){
            username.setText(user.name);
            image_user.setImageBitmap(getUserImage(user.image));
            video_call_btn.setOnClickListener(v -> callListener.initiateVideoCall(user));

            audio_call_btn.setOnClickListener(view -> callListener.initiateAudioCall(user));
        }

    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CallViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.user_call_layaout, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.CallViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
