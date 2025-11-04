package com.example.davinciconnect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.davinciconnect.R;

public class RoleSelectionActivity extends AppCompatActivity {

    public static final String EXTRA_ROLE = "ROLE"; // "student" | "teacher"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        Button btnAlumno = findViewById(R.id.btnAlumno);
        Button btnProfesor = findViewById(R.id.btnProfesor);

        btnAlumno.setOnClickListener(v -> goToLogin("student"));
        btnProfesor.setOnClickListener(v -> goToLogin("teacher"));
    }

    private void goToLogin(String role){
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra(EXTRA_ROLE, role);
        startActivity(i);
    }
}
