package com.example.heychat.listeners;

import com.example.heychat.models.ChatMessage;

public interface MessageListener {

    void onMessageSelection(Boolean isSelected);
    void onGetMessage(ChatMessage chatMessage);

}
