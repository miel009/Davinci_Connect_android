package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class AsistenciaAdapter extends RecyclerView.Adapter<AsistenciaAdapter.AsistenciaViewHolder> {

    private List<Asistencia> asistenciaList;

    public AsistenciaAdapter(List<Asistencia> asistenciaList) {
        this.asistenciaList = asistenciaList;
    }

    @NonNull
    @Override
    public AsistenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asistencia, parent, false);
        return new AsistenciaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsistenciaViewHolder holder, int position) {
        Asistencia asistencia = asistenciaList.get(position);
        holder.tvMateriaName.setText(asistencia.getMateria());
        holder.tvAsistencia.setText(asistencia.getEstado());
    }

    @Override
    public int getItemCount() {
        return asistenciaList.size();
    }

    static class AsistenciaViewHolder extends RecyclerView.ViewHolder {
        TextView tvMateriaName, tvAsistencia;

        public AsistenciaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMateriaName = itemView.findViewById(R.id.tvMateriaName);
            tvAsistencia = itemView.findViewById(R.id.tvAsistencia);
        }
    }
}
