package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class AsistenciaResponse {
    private Long id;
    private EstudianteResponse estudiante;
    private LocalDate fecha;
    private LocalTime horaRegistro;
    private String estadoAsistencia;
    private UsuarioResponse registradoPor;
}
