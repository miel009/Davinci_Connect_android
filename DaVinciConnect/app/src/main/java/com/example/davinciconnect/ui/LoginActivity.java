package com.example.davinciconnect.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView tvGoRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String selectedRole; // "student" o "teacher"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoRegister = findViewById(R.id.tvRegister);

        // Rol recibido desde la pantalla anterior
        selectedRole = getIntent().getStringExtra(RoleSelectionActivity.EXTRA_ROLE);

        btnLogin.setOnClickListener(v -> loginUser());

        tvGoRegister.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            i.putExtra(RoleSelectionActivity.EXTRA_ROLE, selectedRole);
            startActivity(i);
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
                        // tras login, consultamos Firestore para saber el rol real del usuario
                        String uid = mAuth.getCurrentUser().getUid();
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(this::routeByRole)
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Error obteniendo usuario", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void routeByRole(DocumentSnapshot doc){
        if (!doc.exists()) {
            Toast.makeText(this, "Perfil no encontrado. Completa el registro.", Toast.LENGTH_LONG).show();
            // Si el usuario no tiene documento, lo mandamos a registro con el rol seleccionado
            Intent i = new Intent(this, RegisterActivity.class);
            i.putExtra(RoleSelectionActivity.EXTRA_ROLE, selectedRole);
            startActivity(i);
            finish();
            return;
        }

        String role = doc.getString("role"); // "student" o "teacher"
        if ("teacher".equals(role)) {
            startActivity(new Intent(this, TeacherMenuActivity.class));
        } else if ("student".equals(role)) {
            startActivity(new Intent(this, StudentMenuActivity.class));
        } else {
            Toast.makeText(this, "Rol no válido. Contacta al admin.", Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }
}
