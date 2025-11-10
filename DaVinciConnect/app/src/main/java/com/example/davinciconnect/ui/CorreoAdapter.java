package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class CorreoAdapter extends RecyclerView.Adapter<CorreoAdapter.CorreoViewHolder> {

    private List<Correo> correoList;

    public CorreoAdapter(List<Correo> correoList) {
        this.correoList = correoList;
    }

    @NonNull
    @Override
    public CorreoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_correo, parent, false);
        return new CorreoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CorreoViewHolder holder, int position) {
        Correo correo = correoList.get(position);
        holder.tvAsunto.setText(correo.getAsunto());
        holder.tvRemitente.setText(correo.getRemitente());
    }

    @Override
    public int getItemCount() {
        return correoList.size();
    }

    static class CorreoViewHolder extends RecyclerView.ViewHolder {
        TextView tvAsunto, tvRemitente;

        public CorreoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAsunto = itemView.findViewById(R.id.tvAsunto);
            tvRemitente = itemView.findViewById(R.id.tvRemitente);
        }
    }
}
