package com.lemas.asistincialemas.model;

public class AsistenciaResponse {
    private Long id;
    private EstudianteResponse estudiante;
    private String fecha;
    private String horaRegistro;
    private String estadoAsistencia;
    private UsuarioResponse registradoPor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstudianteResponse getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteResponse estudiante) {
        this.estudiante = estudiante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public UsuarioResponse getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(UsuarioResponse registradoPor) {
        this.registradoPor = registradoPor;
    }
}
