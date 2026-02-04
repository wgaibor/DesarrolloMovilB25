package com.lemas.asistincialemas.model;

public class LoginRequest {
    private String cedula;
    private String password;

    public LoginRequest(String cedula, String password) {
        this.cedula = cedula;
        this.password = password;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
