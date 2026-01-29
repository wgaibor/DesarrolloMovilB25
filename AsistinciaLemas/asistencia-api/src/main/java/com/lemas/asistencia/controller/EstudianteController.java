package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.AsistenciaResponse;
import com.lemas.asistencia.model.dto.QrResponse;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final AsistenciaService asistenciaService;

    @GetMapping("/mi-qr")
    public ResponseEntity<QrResponse> obtenerMiQr() {
        return ResponseEntity.ok(asistenciaService.obtenerMiQr());
    }

    @GetMapping("/mis-asistencias")
    public ResponseEntity<List<AsistenciaResponse>> obtenerMisAsistencias(
            @RequestParam(required = false) EstadoAsistencia estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(asistenciaService.obtenerMisAsistencias(estado, desde, hasta));
    }
}
