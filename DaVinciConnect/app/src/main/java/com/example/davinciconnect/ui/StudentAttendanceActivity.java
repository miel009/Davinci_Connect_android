package com.example.davinciconnect.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceActivity extends AppCompatActivity {

    public static final String EXTRA_MATERIA_NAME = "EXTRA_MATERIA_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        String materiaName = getIntent().getStringExtra(EXTRA_MATERIA_NAME);

        TextView tvMateriaTitle = findViewById(R.id.tvMateriaTitle);
        tvMateriaTitle.setText("Asistencia - " + materiaName);

        RecyclerView rvStudentAttendance = findViewById(R.id.rvStudentAttendance);
        rvStudentAttendance.setLayoutManager(new LinearLayoutManager(this));

        List<StudentAttendance> studentList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            studentList.add(new StudentAttendance("Alumno " + i));
        }

        StudentAttendanceAdapter adapter = new StudentAttendanceAdapter(studentList);
        rvStudentAttendance.setAdapter(adapter);
    }
}
