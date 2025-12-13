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
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ImageView imageHeader;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        selectedRole = getIntent().getStringExtra(RoleSelectionActivity.EXTRA_ROLE);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        imageHeader = findViewById(R.id.image_header);

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
                    if (!task.isSuccessful()) {

                        String msg = "Error al iniciar sesión.";
                        String err = (task.getException() != null) ? task.getException().getMessage() : null;

                        if (err != null) {
                            if (err.contains("no user record") || err.contains("user may have been deleted")) {
                                msg = "La cuenta no existe. Registrate para continuar.";
                            } else if (err.contains("password is invalid")) {
                                msg = "La contraseña es incorrecta.";
                            } else if (err.contains("email address is badly formatted")) {
                                msg = "El email ingresado no es válido.";
                            } else if (err.contains("The supplied auth credential is incorrect")) {
                                msg = "Email o contraseña incorrectos.";
                            }
                        }

                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Login OK → ahora verif Firestore (approved)
                    verificarEstadoUsuario();
                });
    }

    private void verificarEstadoUsuario() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Sesión inválida. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {

                    // no existe perfil en Firestore
                    if (!doc.exists()) {
                        mAuth.signOut();
                        Toast.makeText(
                                this,
                                "La cuenta no existe o el registro no se completó. Volvé a registrarte.",
                                Toast.LENGTH_LONG
                        ).show();
                        startActivity(new Intent(this, RoleSelectionActivity.class));
                        finish();
                        return;
                    }

                    Boolean approved = doc.getBoolean("approved");
                    String roleFromDb = doc.getString("role");

                    // pendiente
                    if (approved == null || !approved) {
                        mAuth.signOut();
                        Toast.makeText(this, "Cuenta pendiente de aprobación.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, PendingApprovalActivity.class));
                        finish();
                        return;
                    }

                    //  aprobado → entrar según rol real
                    if ("teacher".equals(roleFromDb)) {
                        startActivity(new Intent(this, TeacherMenuActivity.class));
                    } else {
                        startActivity(new Intent(this, StudentMenuActivity.class));
                    }
                    finish();
                })
                .addOnFailureListener(e -> {
                    mAuth.signOut();
                    Toast.makeText(this, "Error verificando acceso: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
