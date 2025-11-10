package com.example.davinciconnect.ui;

public class OrdenPago {
    private String mes;
    private String estado;

    public OrdenPago(String mes, String estado) {
        this.mes = mes;
        this.estado = estado;
    }

    public String getMes() {
        return mes;
    }

    public String getEstado() {
        return estado;
    }
}
