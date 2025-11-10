package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class ClasesAdapter extends RecyclerView.Adapter<ClasesAdapter.ClaseViewHolder> {

    private List<String> clasesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String clase);
    }

    public ClasesAdapter(List<String> clasesList, OnItemClickListener listener) {
        this.clasesList = clasesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clase, parent, false);
        return new ClaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaseViewHolder holder, int position) {
        String clase = clasesList.get(position);
        holder.tvClaseName.setText(clase);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(clase));
    }

    @Override
    public int getItemCount() {
        return clasesList.size();
    }

    static class ClaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvClaseName;

        public ClaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClaseName = itemView.findViewById(R.id.tvClaseName);
        }
    }
}
