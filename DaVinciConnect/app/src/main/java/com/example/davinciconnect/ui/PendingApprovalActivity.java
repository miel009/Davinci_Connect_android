package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.davinciconnect.R;
import com.google.firebase.auth.FirebaseAuth;

public class PendingApprovalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_approval);

        Button btnBack = findViewById(R.id.btnBackToLogin);

        btnBack.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, RoleSelectionActivity.class));
            finish();
        });
    }
}
