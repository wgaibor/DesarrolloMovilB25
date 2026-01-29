package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursoResponse {
    private Long id;
    private String nombre;
    private String nivel;
    private String paralelo;
    private String anioLectivo;
    private String nombreCompleto;
}
