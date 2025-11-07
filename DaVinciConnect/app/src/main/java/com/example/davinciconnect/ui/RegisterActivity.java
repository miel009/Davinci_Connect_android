package com.example.davinciconnect.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPassword;
    private Button btnCreate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String selectedRole; // recibido de RoleSelectionActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        selectedRole = getIntent().getStringExtra(RoleSelectionActivity.EXTRA_ROLE);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("email", email);
                        user.put("role", selectedRole); // 👈 guardamos el rol
                        user.put("createdAt", System.currentTimeMillis());

                        db.collection("users").document(uid).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                                    // Enruta según el rol
                                    if ("teacher".equals(selectedRole)) {
                                        startActivity(new Intent(this, TeacherMenuActivity.class));
                                    } else {
                                        startActivity(new Intent(this, StudentMenuActivity.class));
                                    }
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Error guardando usuario", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
