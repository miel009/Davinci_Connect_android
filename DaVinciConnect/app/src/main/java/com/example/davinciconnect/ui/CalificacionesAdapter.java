package com.example.davinciconnect.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class CalificacionesAdapter extends RecyclerView.Adapter<CalificacionesAdapter.CalificacionViewHolder> {

    private List<Calificacion> calificacionesList;

    public CalificacionesAdapter(List<Calificacion> calificacionesList) {
        this.calificacionesList = calificacionesList;
    }

    @NonNull
    @Override
    public CalificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calificacion, parent, false);
        return new CalificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalificacionViewHolder holder, int position) {
        Calificacion calificacion = calificacionesList.get(position);
        holder.tvSubjectName.setText(calificacion.getSubject());

        holder.gradesContainer.removeAllViews();

        for (Integer grade : calificacion.getGrades()) {
            Context context = holder.itemView.getContext();
            TextView gradeView = new TextView(context);
            gradeView.setText(String.valueOf(grade));

            GradientDrawable circle = new GradientDrawable();
            circle.setShape(GradientDrawable.OVAL);
            circle.setColor(getGradeColor(grade));

            gradeView.setBackground(circle);
            gradeView.setTextColor(Color.WHITE);
            gradeView.setGravity(android.view.Gravity.CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 120);
            params.setMargins(10, 0, 10, 0);
            gradeView.setLayoutParams(params);

            holder.gradesContainer.addView(gradeView);
        }
    }

    private int getGradeColor(int grade) {
        if (grade <= 10) {
            return Color.RED;
        } else if (grade <= 40) {
            float ratio = (grade - 10) / 30f;
            return Color.rgb(255, (int) (165 * ratio), 0);
        } else if (grade <= 60) {
            float ratio = (grade - 40) / 20f;
            return Color.rgb(255, 165 + (int) (90 * ratio), 0);
        } else if (grade <= 80) {
            float ratio = (grade - 60) / 20f;
            return Color.rgb(255 - (int) (255 * ratio), 255 - (int) (155 * ratio), 0);
        } else {
            float ratio = (grade - 80) / 20f;
            return Color.rgb(0, 100 + (int) (155 * ratio), 0);
        }
    }

    @Override
    public int getItemCount() {
        return calificacionesList.size();
    }

    static class CalificacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName;
        LinearLayout gradesContainer;

        public CalificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            gradesContainer = itemView.findViewById(R.id.gradesContainer);
        }
    }
}
