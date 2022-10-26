package com.example.heychat.activities;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.widget.EditText;
import android.widget.Toast;
import com.example.heychat.R;
import com.example.heychat.adapters.PrivateChatAdapter;
import com.example.heychat.listeners.MessageListener;
import com.example.heychat.models.ChatMessage;
import com.example.heychat.models.RoomChat;
import com.example.heychat.ultilities.Constants;
import com.example.heychat.ultilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PrivateChatActivity extends BaseActivity implements MessageListener {

    private FirebaseFirestore database;
    private RoomChat roomChat;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> chatMessages;
    private PrivateChatAdapter chatAdapter;

    private AppCompatImageView imageBack;
    private RecyclerView chatRecyclerView;
    private EditText inputMessage;
    private CardView layoutSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        init();
        setListener();
        listenMessages();
    }


    private void setListener() {

        layoutSend.setOnClickListener(v -> sendMessage());

        imageBack.setOnClickListener(view -> {

            deleteData();

            preferenceManager.remove(Constants.KEY_COLLECTION_ROOM);

            showToast("Delete All Message Successfully");
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void deleteData(){
        database.collection(Constants.KEY_COLLECTION_ROOM)
                .document(roomChat.id)
                .delete();

        database.collection(Constants.KEY_COLLECTION_ROOM)
                .document(roomChat.id)
                .collection(Constants.KEY_ROOM_MEMBER)
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        database.collection(Constants.KEY_COLLECTION_ROOM)
                                .document(roomChat.id)
                                .collection(Constants.KEY_ROOM_MEMBER)
                                .document(queryDocumentSnapshot.getId())
                                .delete();
                    }
                });
        database.collection(Constants.KEY_COLLECTION_ROOM)
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        if (Integer.parseInt(String.valueOf(queryDocumentSnapshot.get(Constants.KEY_AMOUNT_OF_ROOM))) == 0) {
                            database.collection(Constants.KEY_COLLECTION_ROOM)
                                    .document(queryDocumentSnapshot.getId())
                                    .delete();
                        }
                    }
                });

        database.collection(Constants.KEY_COLLECTION_PRIVATE_CHAT)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, roomChat.id)
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        database.collection(Constants.KEY_COLLECTION_PRIVATE_CHAT)
                                .document(queryDocumentSnapshot.getId())
                                .delete();
                    }
                });
    }

    private void init() {
        database = FirebaseFirestore.getInstance();
        roomChat = (RoomChat) getIntent().getSerializableExtra(Constants.KEY_COLLECTION_ROOM);
        preferenceManager = new PreferenceManager(getApplicationContext());
        imageBack = findViewById(R.id.imageBack);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        inputMessage = findViewById(R.id.inputeMessage);
        layoutSend = findViewById(R.id.layoutSend);


        chatMessages = new ArrayList<>();
        chatAdapter = new PrivateChatAdapter(
                chatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID),
                this
        );
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setItemAnimator(null);


    }

    private void sendMessage() {
        if (!inputMessage.getText().toString().equals("")){
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_RECEIVER_ID, roomChat.id);
            message.put(Constants.KEY_MESSAGE, inputMessage.getText().toString());
            message.put(Constants.KEY_TIMESTAMP, new Date());
            database.collection(Constants.KEY_COLLECTION_PRIVATE_CHAT).add(message);
            try {
                JSONArray tokens = new JSONArray();
                tokens.put(preferenceManager.getString(Constants.KEY_COLLECTION_ROOM));

                JSONObject data = new JSONObject();
                data.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
                data.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
                data.put(Constants.KEY_FCM_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));
                data.put(Constants.KEY_MESSAGE, inputMessage.getText().toString());

                JSONObject body = new JSONObject();
                body.put(Constants.REMOTE_MSG_DATA, data);
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

//                sendNotification(body.toString());

            } catch (Exception e) {
                //showToast(e.getMessage());
            }
        }


        inputMessage.setText(null);
    }

    private void listenMessages() {

        database.collection(Constants.KEY_COLLECTION_PRIVATE_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, roomChat.id)
                .addSnapshotListener(eventListener);


        database.collection(Constants.KEY_COLLECTION_PRIVATE_CHAT)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, roomChat.id)
                .addSnapshotListener(eventListener);

    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dataObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }

            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dataObject.compareTo(obj2.dataObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }

        }
    };

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("dd MMMM, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMessageSelection(Boolean isSelected, int position, List<ChatMessage> chatMessages, ChatMessage chatMessage) {

    }


    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
        deleteData();
    }
}