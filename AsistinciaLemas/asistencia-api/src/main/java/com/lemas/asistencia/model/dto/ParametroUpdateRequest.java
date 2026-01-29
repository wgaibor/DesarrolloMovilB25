package com.lemas.asistencia.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParametroUpdateRequest {
    @NotBlank(message = "El valor es requerido")
    private String valor;
}
