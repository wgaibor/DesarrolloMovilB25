package com.lemas.asistencia.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrResponse {
    private String codigoQr;
    private String nombreEstudiante;
    private String curso;
}
