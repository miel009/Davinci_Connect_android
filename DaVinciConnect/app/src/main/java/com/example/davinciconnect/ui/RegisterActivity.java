package com.example.davinciconnect.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPassword;
    private Button btnCreate;
    private TextView tvLogin;
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
        tvLogin = findViewById(R.id.tvLogin);

        btnCreate.setOnClickListener(v -> createAccount());

        // Make "Inicia Sesión" clickable
        String fullText = "¿Ya tienes una cuenta? Inicia Sesión.";
        String linkText = "Inicia Sesión";
        SpannableString ss = new SpannableString(fullText);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                startActivity(new Intent(RegisterActivity.this, RoleSelectionActivity.class));
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#B39DDB")); // Light Violet
            }
        };

        int startIndex = fullText.indexOf(linkText);
        int endIndex = startIndex + linkText.length();
        ss.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvLogin.setText(ss);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvLogin.setHighlightColor(Color.TRANSPARENT);
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
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    String uid = mAuth.getCurrentUser().getUid();

                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("role", selectedRole);
                    user.put("createdAt", System.currentTimeMillis());
                    user.put("approved", false);
                    user.put("status", "pending");

                    // guardar usuario
                    db.collection("users").document(uid).set(user)
                            .addOnSuccessListener(aVoid -> {
                                // guardar solicitud
                                Map<String, Object> sol = new HashMap<>();
                                sol.put("uid", uid);
                                sol.put("email", email);
                                sol.put("role", selectedRole);
                                sol.put("createdAt", System.currentTimeMillis());
                                sol.put("status", "pending");

                                db.collection("solicitudes").document(uid).set(sol)
                                        .addOnSuccessListener(v -> {
                                            Toast.makeText(this, "Cuenta creada. Esperando aprobación.", Toast.LENGTH_SHORT).show();
                                            // cerrar sesión para que no entre
                                            mAuth.signOut();
                                            //  enviar a pantalla “pendiente”
                                            startActivity(new Intent(this, PendingApprovalActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(this, "Error creando solicitud", Toast.LENGTH_SHORT).show()
                                        );
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Error guardando usuario", Toast.LENGTH_SHORT).show()
                            );
                });
}
}