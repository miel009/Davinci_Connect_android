package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;

import java.util.List;

public class AttendanceStatusAdapter extends RecyclerView.Adapter<AttendanceStatusAdapter.ViewHolder> {

    private List<AttendanceStatus> attendanceList;

    public AttendanceStatusAdapter(List<AttendanceStatus> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceStatus item = attendanceList.get(position);
        holder.subjectTextView.setText(item.getSubject());
        holder.statusTextView.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subjectTextView;
        public TextView statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}
