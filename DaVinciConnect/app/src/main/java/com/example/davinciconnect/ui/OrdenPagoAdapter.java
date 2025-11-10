package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class OrdenPagoAdapter extends RecyclerView.Adapter<OrdenPagoAdapter.OrdenPagoViewHolder> {

    private List<OrdenPago> ordenPagoList;

    public OrdenPagoAdapter(List<OrdenPago> ordenPagoList) {
        this.ordenPagoList = ordenPagoList;
    }

    @NonNull
    @Override
    public OrdenPagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orden_pago, parent, false);
        return new OrdenPagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdenPagoViewHolder holder, int position) {
        OrdenPago ordenPago = ordenPagoList.get(position);
        holder.tvMes.setText(ordenPago.getMes());
        holder.tvEstado.setText(ordenPago.getEstado());
    }

    @Override
    public int getItemCount() {
        return ordenPagoList.size();
    }

    static class OrdenPagoViewHolder extends RecyclerView.ViewHolder {
        TextView tvMes, tvEstado;

        public OrdenPagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMes = itemView.findViewById(R.id.tvMes);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
