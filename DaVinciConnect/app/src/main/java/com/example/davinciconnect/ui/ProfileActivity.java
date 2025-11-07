package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private EditText etDisplayName, etEmail, etPassword, etPin;
    private Button btnUpdateDisplayName, btnUpdateEmail, btnUpdatePassword, btnUpdatePin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        etDisplayName = findViewById(R.id.etDisplayName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPin = findViewById(R.id.etPin);

        btnUpdateDisplayName = findViewById(R.id.btnUpdateDisplayName);
        btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnUpdatePin = findViewById(R.id.btnUpdatePin);

        loadUserProfile();

        btnUpdateDisplayName.setOnClickListener(v -> updateDisplayName());
        btnUpdateEmail.setOnClickListener(v -> updateEmail());
        btnUpdatePassword.setOnClickListener(v -> updatePassword());
        btnUpdatePin.setOnClickListener(v -> updatePin());
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            etDisplayName.setText(currentUser.getDisplayName());
            etEmail.setText(currentUser.getEmail());
            db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("pin")) {
                        etPin.setText(documentSnapshot.getString("pin"));
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading PIN", e));
        }
    }

    private void updateDisplayName() {
        String displayName = etDisplayName.getText().toString().trim();
        if (displayName.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Nombre actualizado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error al actualizar el nombre.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateEmail() {
        String email = etEmail.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor, introduce un email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Email actualizado. Verifica tu nueva dirección y vuelve a iniciar sesión.", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e(TAG, "Error updating email", task.getException());
                        Toast.makeText(ProfileActivity.this, "Error al actualizar el email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updatePassword() {
        String password = etPassword.getText().toString().trim();
        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        etPassword.setText("");
                        Toast.makeText(ProfileActivity.this, "Contraseña actualizada.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Error updating password", task.getException());
                        Toast.makeText(ProfileActivity.this, "Error al actualizar la contraseña: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updatePin() {
        String pin = etPin.getText().toString().trim();
        if (pin.isEmpty()) {
            Toast.makeText(this, "El PIN no puede estar vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(currentUser.getUid())
                .update("pin", pin)
                .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "PIN actualizado.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Error al actualizar el PIN.", Toast.LENGTH_SHORT).show());
    }
}
