package com.example.davinciconnect.ui;

public class Event {
    private String id;
    private String description;
    private String time;

    // Firebase requiere un constructor vacío
    public Event() {}

    public Event(String id, String description, String time) {
        this.id = id;
        this.description = description;
        this.time = time;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
