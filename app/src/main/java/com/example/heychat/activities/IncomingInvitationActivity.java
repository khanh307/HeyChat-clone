package com.example.heychat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heychat.R;
import com.example.heychat.network.ApiClient;
import com.example.heychat.network.ApiService;
import com.example.heychat.service.SinchService;
import com.example.heychat.ultilities.Constants;
import com.example.heychat.ultilities.PreferenceManager;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

import org.json.JSONArray;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingInvitationActivity extends BaseSinchActivity {

    private MediaPlayer music;
    private String meetingType = null;
    private PreferenceManager preferenceManager;
    private String callId;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitation);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        ImageView imageMeetingType = findViewById(R.id.imageMeetingType);
        meetingType = getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);

        if(meetingType != null){
            if(meetingType.equals("video")){
                imageMeetingType.setImageResource(R.drawable.ic_video_call);
            } else {
                imageMeetingType.setImageResource(R.drawable.ic_call);
            }
        }

        CircleImageView textFirstChar = findViewById(R.id.image_user_incoming);
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textEmail = findViewById(R.id.incomingtextEmail);

        textUsername.setText(
                getIntent().getStringExtra(Constants.KEY_NAME)
        );

        textEmail.setText(
                getIntent().getStringExtra(Constants.KEY_EMAIL)
        );

        callId = getIntent().getStringExtra(SinchService.CALL_ID);

        ImageView imageAcceptInvitation = findViewById(R.id.imageAcceptInvitaion);
        imageAcceptInvitation.setOnClickListener(v -> sendInvitationResponse(
                Constants.REMOTE_MSG_INVITATION_ACCEPTED,
                getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)
        ));

        ImageView imageRejectInvitation = findViewById(R.id.imageRejectInvitaion);
        imageRejectInvitation.setOnClickListener(v -> sendInvitationResponse(
                Constants.REMOTE_MSG_INVITATION_REJECTED,
                getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)
        ));

    }

    private void sendInvitationResponse(String type, String receiverToken){
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), type);
        } catch (Exception exception){
            Toast.makeText(IncomingInvitationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    private void sendRemoteMessage(String remoteMesageBody, String type){
        ApiClient.getClient().create(ApiService.class).sendMessage(Constants.getRemoteMsgHeaders(), remoteMesageBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if(response.isSuccessful()){
                            if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)){
                                try {
//                                    URL serverURL = new URL("https://meet.jit.si");
//
//                                    JitsiMeetConferenceOptions.Builder builder = new JitsiMeetConferenceOptions.Builder();
//                                    builder.setServerURL(serverURL);
//                                    builder.setWelcomePageEnabled(false);
//                                    builder.setRoom(getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM));
//
//                                    if(meetingType.equals("audio")){
//                                        builder.setAudioOnly(true);
//                                    }
//
//                                    JitsiMeetActivity.launch(IncomingInvitationActivity.this, builder.build());
                                    Toast.makeText(IncomingInvitationActivity.this, "Accepted: "+ callId, Toast.LENGTH_SHORT).show();
                                    music.stop();

                                   com.sinch.android.rtc.calling.Call sinchCall = getSinchServiceInterface().getCall(callId);

                                    if (sinchCall != null) {
                                        sinchCall.answer();
                                        Intent intent = new Intent(IncomingInvitationActivity.this, VideoCallActivity.class);
                                        intent.putExtra(SinchService.CALL_ID, callId);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        finish();
                                    }

                                } catch (Exception exception){
                                    Toast.makeText(IncomingInvitationActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            } else {
                                Toast.makeText(IncomingInvitationActivity.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                                getSinchServiceInterface().stopClient();
                                finish();
                            }
                        } else {
                            Toast.makeText(IncomingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                        Toast.makeText(IncomingInvitationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1){
//            if (resultCode == RESULT_OK){
//                String myStr = data.getStringExtra("endcall");
//                if (myStr != null){
//                    finish();
//                }
//            }
//        }
//    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            preferenceManager = new PreferenceManager(getApplicationContext());
            String userName = preferenceManager.getString(Constants.KEY_USER_ID);
            SinchClient mSinchClient;
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(SinchService.APP_KEY)
                    .applicationSecret(SinchService.APP_SECRET)
                    .environmentHost(SinchService.ENVIRONMENT).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection();
            mSinchClient.start();

            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if(type != null){
                if (type.equals(Constants.REMOTE_MSG_INVITATION_CANCELLED)){
                    Toast.makeText(IncomingInvitationActivity.this, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
                    getSinchServiceInterface().stopClient();
                    finish();
                }
            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("BBB", "onStart");
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        music = MediaPlayer.create(getApplicationContext(), R.raw.notification);
        music.setLooping(true);
        music.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        music.stop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(
                invitationResponseReceiver
        );
    }


    private Bitmap getUserImage(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }
}