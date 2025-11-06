package com.example.davinciconnect.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.davinciconnect.R;

public class ProfileActivity extends AppCompatActivity {

    private EditText etDisplayName, etEmail, etPassword, etPin;
    private Button btnUpdateDisplayName, btnUpdateEmail, btnUpdatePassword, btnUpdatePin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etDisplayName = findViewById(R.id.etDisplayName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPin = findViewById(R.id.etPin);

        btnUpdateDisplayName = findViewById(R.id.btnUpdateDisplayName);
        btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnUpdatePin = findViewById(R.id.btnUpdatePin);

        // Lógica de los botones se implementará en el siguiente paso
    }
}
