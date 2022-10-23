package com.example.heychat.listeners;

import com.example.heychat.models.ChatMessage;

import java.util.List;

public interface MessageListener {

    void onMessageSelection(Boolean isSelected);
    void onTranslateMessage(ChatMessage chatMessage, int pos);
    void onDeleteMessage(ChatMessage chatMessage, int pos, List<ChatMessage> chatMessages);

}
