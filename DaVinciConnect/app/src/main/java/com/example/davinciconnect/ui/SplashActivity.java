package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private final long SPLASH_DELAY = 1200L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(this::decideNext, SPLASH_DELAY);
    }

    private void decideNext() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        // Hay sesión -> consultar rol
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users").document(uid).get()
                .addOnSuccessListener(this::routeByRole)
                .addOnFailureListener(e -> {
                    // Si falla, mandar al login
                    startActivity(new Intent(this, WelcomeActivity.class));
                    finish();
                });
    }

    private void routeByRole(DocumentSnapshot doc) {
        if (!doc.exists()) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }
        String role = doc.getString("role");
        if ("teacher".equals(role)) {
            startActivity(new Intent(this, TeacherMenuActivity.class));
        } else {
            startActivity(new Intent(this, StudentMenuActivity.class));
        }
        finish();
    }
}
