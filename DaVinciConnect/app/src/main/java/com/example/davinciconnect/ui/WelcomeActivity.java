package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.davinciconnect.R;
import com.example.davinciconnect.ui.ThemeManager;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_welcome);

        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v ->
                startActivity(new Intent(this, RoleSelectionActivity.class))
        );
    }
}
