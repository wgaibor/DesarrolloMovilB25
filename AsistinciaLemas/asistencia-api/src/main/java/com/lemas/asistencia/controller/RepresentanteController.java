package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.AsistenciaResponse;
import com.lemas.asistencia.model.dto.EstudianteResponse;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/representantes")
@RequiredArgsConstructor
public class RepresentanteController {

    private final AsistenciaService asistenciaService;

    @GetMapping("/mis-estudiantes")
    public ResponseEntity<List<EstudianteResponse>> obtenerMisEstudiantes() {
        return ResponseEntity.ok(asistenciaService.obtenerMisEstudiantes());
    }

    @GetMapping("/estudiantes/{estudianteId}/asistencias")
    public ResponseEntity<List<AsistenciaResponse>> obtenerAsistenciasEstudiante(
            @PathVariable Long estudianteId,
            @RequestParam(required = false) EstadoAsistencia estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(asistenciaService.obtenerAsistenciasEstudiante(estudianteId, estado, desde, hasta));
    }
}
