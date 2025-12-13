package com.example.davinciconnect.ui;

public class AttendanceStatus {
    private String subject;
    private String status;

    public AttendanceStatus(String subject, String status) {
        this.subject = subject;
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public String getStatus() {
        return status;
    }
}
