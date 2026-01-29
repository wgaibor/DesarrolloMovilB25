package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParametroResponse {
    private Long id;
    private String clave;
    private String valor;
    private String descripcion;
}
