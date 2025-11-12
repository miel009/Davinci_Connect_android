package com.example.davinciconnect.ui;

import java.util.List;

public class Calificacion {
    private String subject;
    private List<Integer> grades;

    public Calificacion(String subject, List<Integer> grades) {
        this.subject = subject;
        this.grades = grades;
    }

    public String getSubject() {
        return subject;
    }

    public List<Integer> getGrades() {
        return grades;
    }
}
