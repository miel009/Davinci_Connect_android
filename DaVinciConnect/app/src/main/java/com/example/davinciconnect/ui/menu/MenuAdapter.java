package com.example.davinciconnect.ui.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

    public interface OnItemClick {
        void onClick(MenuItemModel item, int position);
    }

    private final List<MenuItemModel> data = new ArrayList<>();
    private final OnItemClick listener;

    public MenuAdapter(OnItemClick listener) { this.listener = listener; }

    public void submit(List<MenuItemModel> items){
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        MenuItemModel it = data.get(pos);
        h.icon.setImageResource(it.iconRes);
        h.label.setText(it.label);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(it, pos);
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView icon; TextView label;
        VH(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imgIcon);
            label = itemView.findViewById(R.id.tvLabel);
        }
    }
}
