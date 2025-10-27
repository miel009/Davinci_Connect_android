package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.davinciconnect.R;

public class MenuActivity extends AppCompatActivity {

    private ImageView imgChatLeo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_menu);

        imgChatLeo = findViewById(R.id.imgIcon);

        imgChatLeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ChatLeoIntroActivity.class);
                startActivity(intent);
            }
        });
    }
}
