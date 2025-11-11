package com.example.davinciconnect.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ImageView imageHeader;
    private FirebaseAuth mAuth;
    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        selectedRole = getIntent().getStringExtra(RoleSelectionActivity.EXTRA_ROLE);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        imageHeader = findViewById(R.id.image_header);

        // Set the header image based on the role
        if ("teacher".equals(selectedRole)) {
            imageHeader.setImageResource(R.drawable.profesor);
        } else {
            imageHeader.setImageResource(R.drawable.alumnos);
        }

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra(RoleSelectionActivity.EXTRA_ROLE, selectedRole);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Navigate to the corresponding menu based on the role
                        if ("teacher".equals(selectedRole)) {
                            startActivity(new Intent(this, TeacherMenuActivity.class));
                        } else {
                            startActivity(new Intent(this, StudentMenuActivity.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(this, "Error en el inicio de sesión: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
