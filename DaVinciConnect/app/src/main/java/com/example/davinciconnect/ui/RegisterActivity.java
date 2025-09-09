package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText etEmailRegister, etPasswordRegister;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        etEmailRegister = findViewById(R.id.etEmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = etEmailRegister.getText().toString().trim();
                String pwd = etPasswordRegister.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    etEmailRegister.setError("Ingrese un email");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    etPasswordRegister.setError("Ingrese una contraseña");
                    return;
                }
                if (pwd.length() < 6) {
                    etPasswordRegister.setError("La contraseña debe tener al menos 6 caracteres");
                    return;
                }

                auth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
