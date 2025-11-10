package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class CapacitacionAdapter extends RecyclerView.Adapter<CapacitacionAdapter.CapacitacionViewHolder> {

    private List<Capacitacion> capacitacionList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Capacitacion capacitacion);
    }

    public CapacitacionAdapter(List<Capacitacion> capacitacionList, OnItemClickListener listener) {
        this.capacitacionList = capacitacionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CapacitacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_capacitacion, parent, false);
        return new CapacitacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CapacitacionViewHolder holder, int position) {
        Capacitacion capacitacion = capacitacionList.get(position);
        holder.tvNombre.setText(capacitacion.getNombre());
        holder.tvFecha.setText(capacitacion.getFecha());
        holder.tvHora.setText(capacitacion.getHora());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(capacitacion));
    }

    @Override
    public int getItemCount() {
        return capacitacionList.size();
    }

    static class CapacitacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvFecha, tvHora;

        public CapacitacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvCapacitacionName);
            tvFecha = itemView.findViewById(R.id.tvCapacitacionDate);
            tvHora = itemView.findViewById(R.id.tvCapacitacionTime);
        }
    }
}
