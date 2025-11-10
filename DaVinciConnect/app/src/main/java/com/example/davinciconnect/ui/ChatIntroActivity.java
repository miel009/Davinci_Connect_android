package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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
        //  animacion flotante
        ImageView imgLeonardo = findViewById(R.id.imgIlustracion);
        Animation floatAnim = AnimationUtils.loadAnimation(this, R.anim.anim_float);
        imgLeonardo.startAnimation(floatAnim);

        // volver atras
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()
        );
    }
}
