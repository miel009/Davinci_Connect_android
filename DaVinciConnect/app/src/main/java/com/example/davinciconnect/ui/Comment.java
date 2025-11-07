package com.example.davinciconnect.ui;

import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;

public class Comment {
    private String text;
    private String userId;
    private String userName;
    @ServerTimestamp
    private Date timestamp;

    public Comment() {}

    public Comment(String text, String userId, String userName) {
        this.text = text;
        this.userId = userId;
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
