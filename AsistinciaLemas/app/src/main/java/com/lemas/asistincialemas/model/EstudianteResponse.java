package com.lemas.asistincialemas.model;

public class EstudianteResponse {
    private Long id;
    private String nombre;
    private String cedula;
    private CursoResponse curso;
    private String parentesco;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public CursoResponse getCurso() {
        return curso;
    }

    public void setCurso(CursoResponse curso) {
        this.curso = curso;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}
