package com.example.davinciconnect.ui.chat;

public class ChatMessage {
    public final String text;
    public final boolean fromMe;
    public ChatMessage(String text, boolean fromMe) {
        this.text = text;
        this.fromMe = fromMe;
    }
}
