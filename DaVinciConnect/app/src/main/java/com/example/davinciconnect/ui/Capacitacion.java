package com.example.davinciconnect.ui;

public class Capacitacion {
    private String nombre;
    private String fecha;
    private String hora;

    public Capacitacion(String nombre, String fecha, String hora) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
}
