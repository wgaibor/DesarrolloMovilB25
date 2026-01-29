package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstudianteResponse {
    private Long id;
    private String nombre;
    private String cedula;
    private CursoResponse curso;
    private String parentesco;
}
