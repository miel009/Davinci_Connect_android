package com.example.davinciconnect.ui;

public class Asistencia {
    private String materia;
    private String estado;

    public Asistencia(String materia, String estado) {
        this.materia = materia;
        this.estado = estado;
    }

    public String getMateria() {
        return materia;
    }

    public String getEstado() {
        return estado;
    }
}
