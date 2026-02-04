package com.lemas.asistincialemas.model;

public class RegistrarAsistenciaRequest {
    private String codigoQr;

    public RegistrarAsistenciaRequest(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }
}
