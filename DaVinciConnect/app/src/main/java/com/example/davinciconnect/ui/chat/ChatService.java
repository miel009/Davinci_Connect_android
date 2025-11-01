package com.example.davinciconnect.ui.chat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatService {

    class ChatReq {
        public String text;
        public String uid;

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

    @POST("chatLeo") //
    Call<ChatRes> chat(@Body ChatReq req);
}
