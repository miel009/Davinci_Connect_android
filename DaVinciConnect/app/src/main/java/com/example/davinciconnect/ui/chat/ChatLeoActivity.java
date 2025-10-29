package com.example.davinciconnect.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.davinciconnect.R;
import com.example.davinciconnect.ui.chat.ChatAdapter;
import com.example.davinciconnect.ui.chat.ChatMessage;
import com.example.davinciconnect.ui.chat.ChatService;
import com.example.davinciconnect.ui.chat.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatLeoActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ChatAdapter adapter;
    private EditText input;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_leo);

        rv = findViewById(R.id.rvChat);
        input = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new ChatAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        rv.setAdapter(adapter);

        // Mensaje de bienvenida
        adapter.prepend(new ChatMessage("¡Hola! Soy Leo. ¿Qué deseas consultar hoy? (ej: horarios, materias, aulas, notas…)", false));

        btnSend.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (TextUtils.isEmpty(text)) return;

            adapter.prepend(new ChatMessage(text, true));
            input.setText("");

            ChatService svc = RetrofitClient.get().create(ChatService.class);

            String uid = null;
            try {
                com.google.firebase.auth.FirebaseAuth auth =
                        com.google.firebase.auth.FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) uid = auth.getCurrentUser().getUid();
            } catch (Exception ignored) {}

            svc.chat(new ChatService.ChatReq(text, uid)).enqueue(new Callback<ChatService.ChatRes>() {
                @Override public void onResponse(Call<ChatService.ChatRes> call, Response<ChatService.ChatRes> res) {
                    if (res.isSuccessful() && res.body()!=null) {
                        adapter.prepend(new ChatMessage(res.body().reply, false));
                    } else {
                        adapter.prepend(new ChatMessage("Ups, no pude responder ahora.", false));
                    }
                }
                @Override public void onFailure(Call<ChatService.ChatRes> call, Throwable t) {
                    adapter.prepend(new ChatMessage("Error de red: " + t.getMessage(), false));
                }
            });
        });

    }
}
