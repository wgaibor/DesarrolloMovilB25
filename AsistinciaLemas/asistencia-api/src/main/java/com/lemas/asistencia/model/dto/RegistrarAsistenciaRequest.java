package com.lemas.asistencia.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrarAsistenciaRequest {
    @NotBlank(message = "El código QR es requerido")
    private String codigoQr;
}
