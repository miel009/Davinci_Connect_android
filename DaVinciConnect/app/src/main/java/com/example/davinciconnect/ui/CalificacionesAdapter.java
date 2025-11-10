package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.tvMateriaName.setText(calificacion.getMateria());
        holder.tvNota.setText(calificacion.getNota());
    }

    @Override
    public int getItemCount() {
        return calificacionesList.size();
    }

    static class CalificacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvMateriaName, tvNota;

        public CalificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMateriaName = itemView.findViewById(R.id.tvMateriaName);
            tvNota = itemView.findViewById(R.id.tvNota);
        }
    }
}
