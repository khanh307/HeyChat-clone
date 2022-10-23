package com.example.heychat.models;

import com.example.heychat.ultilities.Constants;

import java.util.Date;

public class ChatMessage {
    public String type = Constants.MESSAGE_TEXT;
    public String senderId, receiverId, message, dateTime;
    public Date dataObject;
    public String conversionId, conversionName, conversionImage;
    public Boolean isSelected = false;

    public ChatMessage() {
        type = Constants.MESSAGE_TEXT;
    }
}
