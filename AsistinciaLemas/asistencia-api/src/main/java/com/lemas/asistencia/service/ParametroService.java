package com.lemas.asistencia.service;

import com.lemas.asistencia.exception.ResourceNotFoundException;
import com.lemas.asistencia.model.dto.ParametroResponse;
import com.lemas.asistencia.model.dto.ParametroUpdateRequest;
import com.lemas.asistencia.model.entity.Parametro;
import com.lemas.asistencia.repository.ParametroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParametroService {

    private final ParametroRepository parametroRepository;

    @Transactional(readOnly = true)
    public List<ParametroResponse> listarTodos() {
        return parametroRepository.findAll().stream()
                .filter(p -> "A".equals(p.getEstado()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParametroResponse actualizar(String clave, ParametroUpdateRequest request) {
        Parametro parametro = parametroRepository.findByClaveAndEstado(clave, "A")
                .orElseThrow(() -> new ResourceNotFoundException("Parámetro no encontrado: " + clave));

        parametro.setValor(request.getValor());
        parametro = parametroRepository.save(parametro);

        return toResponse(parametro);
    }

    @Transactional(readOnly = true)
    public LocalTime getHoraLimiteAtraso() {
        return parametroRepository.findByClaveAndEstado("hora_limite_atraso", "A")
                .map(p -> LocalTime.parse(p.getValor()))
                .orElse(LocalTime.of(7, 30));
    }

    @Transactional(readOnly = true)
    public LocalTime getHoraCierreAsistencia() {
        return parametroRepository.findByClaveAndEstado("hora_cierre_asistencia", "A")
                .map(p -> LocalTime.parse(p.getValor()))
                .orElse(LocalTime.of(9, 0));
    }

    private ParametroResponse toResponse(Parametro parametro) {
        return ParametroResponse.builder()
                .id(parametro.getId())
                .clave(parametro.getClave())
                .valor(parametro.getValor())
                .descripcion(parametro.getDescripcion())
                .build();
    }
}
