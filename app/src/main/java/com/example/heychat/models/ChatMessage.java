package com.example.heychat.models;

import com.example.heychat.ultilities.Constants;

import java.util.Date;

public class ChatMessage {
    public String type = Constants.MESSAGE_TEXT;
    public String id, senderId, receiverId, message, dateTime;
    public Date dataObject;
    public String conversionId, conversionName, conversionImage;
<<<<<<< HEAD
    public Boolean isSelected = false;
=======
    public Boolean isSelected = false, isSeen;
    public String model;
    public Boolean lastReceiver = false;
>>>>>>> 8e766fd421345a25f6ea4e1f1d73b281b5c00909

    public ChatMessage() {
        type = Constants.MESSAGE_TEXT;
    }
}
