package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {
    private Long id;
    private String cedula;
    private String nombre;
    private String email;
    private String rol;
}
