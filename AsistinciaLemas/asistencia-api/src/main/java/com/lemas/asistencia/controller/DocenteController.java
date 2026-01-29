package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.*;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final AsistenciaService asistenciaService;

    @GetMapping("/mis-cursos")
    public ResponseEntity<List<CursoResponse>> obtenerMisCursos() {
        return ResponseEntity.ok(asistenciaService.obtenerMisCursos());
    }

    @GetMapping("/cursos/{cursoId}/estudiantes")
    public ResponseEntity<List<EstudianteResponse>> obtenerEstudiantesCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(asistenciaService.obtenerEstudiantesCurso(cursoId));
    }

    @PostMapping("/asistencia")
    public ResponseEntity<AsistenciaResponse> registrarAsistencia(
            @Valid @RequestBody RegistrarAsistenciaRequest request) {
        return ResponseEntity.ok(asistenciaService.registrarAsistencia(request));
    }

    @GetMapping("/cursos/{cursoId}/asistencias")
    public ResponseEntity<List<AsistenciaResponse>> obtenerAsistenciasCurso(
            @PathVariable Long cursoId,
            @RequestParam(required = false) EstadoAsistencia estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(asistenciaService.obtenerAsistenciasCurso(cursoId, estado, fecha));
    }
}
