package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.davinciconnect.R;
import com.example.davinciconnect.ui.chat.ChatLeoActivity;

public class ChatIntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_chat_intro);

        Button btn = findViewById(R.id.btnComenzarChat);
        btn.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatLeoActivity.class));
        });
    }
}
