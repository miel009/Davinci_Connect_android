package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceViewHolder> {

    private List<StudentAttendance> studentList;

    public StudentAttendanceAdapter(List<StudentAttendance> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentAttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_attendance, parent, false);
        return new StudentAttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAttendanceViewHolder holder, int position) {
        StudentAttendance student = studentList.get(position);
        holder.tvStudentName.setText(student.getStudentName());

        holder.cbPresente.setOnCheckedChangeListener(null);
        holder.cbAusente.setOnCheckedChangeListener(null);

        holder.cbPresente.setChecked(student.isPresent());
        holder.cbAusente.setChecked(student.isAbsent());

        holder.cbPresente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setPresent(isChecked);
            if (isChecked) {
                student.setAbsent(false);
                holder.cbAusente.setChecked(false);
            }
        });

        holder.cbAusente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setAbsent(isChecked);
            if (isChecked) {
                student.setPresent(false);
                holder.cbPresente.setChecked(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentAttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        CheckBox cbPresente, cbAusente;

        public StudentAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            cbPresente = itemView.findViewById(R.id.cbPresente);
            cbAusente = itemView.findViewById(R.id.cbAusente);
        }
    }
}
