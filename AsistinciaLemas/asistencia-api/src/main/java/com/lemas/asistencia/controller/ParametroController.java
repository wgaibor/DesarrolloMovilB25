package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.ParametroResponse;
import com.lemas.asistencia.model.dto.ParametroUpdateRequest;
import com.lemas.asistencia.service.ParametroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parametros")
@RequiredArgsConstructor
public class ParametroController {

    private final ParametroService parametroService;

    @GetMapping
    public ResponseEntity<List<ParametroResponse>> listarTodos() {
        return ResponseEntity.ok(parametroService.listarTodos());
    }

    @PutMapping("/{clave}")
    public ResponseEntity<ParametroResponse> actualizar(
            @PathVariable String clave,
            @Valid @RequestBody ParametroUpdateRequest request) {
        return ResponseEntity.ok(parametroService.actualizar(clave, request));
    }
}
