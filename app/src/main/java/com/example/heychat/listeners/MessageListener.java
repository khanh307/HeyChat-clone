package com.example.heychat.listeners;

import com.example.heychat.models.ChatMessage;

<<<<<<< HEAD
public interface MessageListener {

    void onMessageSelection(Boolean isSelected);
    void onGetMessage(ChatMessage chatMessage);
=======
import java.util.List;

public interface MessageListener {

    void onMessageSelection(Boolean isSelected);
    void onTranslateMessage(ChatMessage chatMessage, int pos);
    void onDeleteMessage(ChatMessage chatMessage, int pos, List<ChatMessage> chatMessages);
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909

}
