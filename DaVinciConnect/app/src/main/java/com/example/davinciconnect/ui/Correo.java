package com.example.davinciconnect.ui;

public class Correo {
    private String asunto;
    private String remitente;

    public Correo(String asunto, String remitente) {
        this.asunto = asunto;
        this.remitente = remitente;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getRemitente() {
        return remitente;
    }
}
