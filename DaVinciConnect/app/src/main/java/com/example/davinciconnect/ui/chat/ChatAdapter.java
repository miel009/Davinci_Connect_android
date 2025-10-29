package com.example.davinciconnect.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ME = 1;
    private static final int BOT = 2;

    private final List<ChatMessage> data = new ArrayList<>();

    public void prepend(ChatMessage m){
        data.add(0, m);
        notifyItemInserted(0);
    }

    @Override public int getItemViewType(int position) {
        return data.get(position).fromMe ? ME : BOT;
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (viewType == ME) ? R.layout.item_chat_me : R.layout.item_chat_bot;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage m = data.get(position);
        ((VH)holder).tv.setText(m.text);
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(View v){ super(v); tv = v.findViewById(R.id.tvMsg); }
    }
}
