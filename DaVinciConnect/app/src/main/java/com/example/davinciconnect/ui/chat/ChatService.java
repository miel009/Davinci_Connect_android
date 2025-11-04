package com.example.davinciconnect.ui.chat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatService {

    // Request/Response simples para Retrofit + Gson
    class ChatReq {
        public String text;
        public String uid;

        // Constructor vacío para Gson (importante)
        public ChatReq() {}

        public ChatReq(String text, String uid) {
            this.text = text;
            this.uid = uid;
        }
        public ChatReq(String text) {
            this.text = text;
        }
    }

    class ChatRes {
        public String reply;
    }

    // Cambia "chatLeo" si tu Function se llama distinto (p.ej. chatWithGemini)
    @POST("chatLeo")
    Call<ChatRes> chat(@Body ChatReq req);
}
