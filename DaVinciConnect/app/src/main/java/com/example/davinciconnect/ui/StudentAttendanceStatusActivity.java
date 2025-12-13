package com.example.davinciconnect.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;

import java.util.ArrayList;
import java.util.List;

public class StudentAttendanceStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_status);

        RecyclerView rvAttendanceStatus = findViewById(R.id.rvAttendanceStatus);
        rvAttendanceStatus.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Replace with actual data from Firebase
        List<AttendanceStatus> attendanceList = new ArrayList<>();
        attendanceList.add(new AttendanceStatus("Materia 1ABC", "Presente"));
        attendanceList.add(new AttendanceStatus("Materia 2ABC", "Ausente"));
        attendanceList.add(new AttendanceStatus("Materia 3ABC", "Presente"));
        attendanceList.add(new AttendanceStatus("Materia 4ABC", "Presente"));
        attendanceList.add(new AttendanceStatus("Materia 5ABC", "Ausente"));

        AttendanceStatusAdapter adapter = new AttendanceStatusAdapter(attendanceList);
        rvAttendanceStatus.setAdapter(adapter);
    }
}
