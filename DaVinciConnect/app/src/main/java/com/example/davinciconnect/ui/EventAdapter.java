package com.example.davinciconnect.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.davinciconnect.R;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnEventListener {
        void onEditClick(Event event);
        void onDeleteClick(Event event);
    }

    private List<Event> eventList;
    private OnEventListener listener;

    public EventAdapter(List<Event> eventList, OnEventListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        String eventInfo = event.getTime() + " - " + event.getDescription();
        holder.tvEventInfo.setText(eventInfo);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(event));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventInfo;
        ImageButton btnEdit, btnDelete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventInfo = itemView.findViewById(R.id.tvEventInfo);
            btnEdit = itemView.findViewById(R.id.btnEditEvent);
            btnDelete = itemView.findViewById(R.id.btnDeleteEvent);
        }
    }
}
