package com.example.davinciconnect.ui;

public class Calificacion {
    private String materia;
    private String nota;

    public Calificacion(String materia, String nota) {
        this.materia = materia;
        this.nota = nota;
    }

    public String getMateria() {
        return materia;
    }

    public String getNota() {
        return nota;
    }
}
