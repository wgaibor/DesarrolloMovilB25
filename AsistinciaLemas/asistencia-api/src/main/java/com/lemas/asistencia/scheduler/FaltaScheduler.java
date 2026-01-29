package com.lemas.asistencia.scheduler;

import com.lemas.asistencia.model.entity.Asistencia;
import com.lemas.asistencia.model.entity.Estudiante;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.repository.AsistenciaRepository;
import com.lemas.asistencia.repository.EstudianteRepository;
import com.lemas.asistencia.service.ParametroService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FaltaScheduler {

    private final EstudianteRepository estudianteRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final ParametroService parametroService;

    // Ejecutar cada minuto para verificar si es hora de marcar faltas
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void marcarFaltasAutomaticas() {
        LocalTime horaCierre = parametroService.getHoraCierreAsistencia();
        LocalTime ahora = LocalTime.now();

        // Solo ejecutar si estamos en la hora de cierre (±1 minuto)
        if (ahora.getHour() != horaCierre.getHour() || ahora.getMinute() != horaCierre.getMinute()) {
            return;
        }

        log.info("Iniciando marcado automático de faltas...");

        LocalDate hoy = LocalDate.now();
        List<Estudiante> todosEstudiantes = estudianteRepository.findAll().stream()
                .filter(e -> "A".equals(e.getEstado()))
                .toList();

        int faltasMarcadas = 0;
        for (Estudiante estudiante : todosEstudiantes) {
            boolean tieneAsistenciaHoy = asistenciaRepository
                    .findByEstudianteIdAndFechaAndEstado(estudiante.getId(), hoy, "A")
                    .isPresent();

            if (!tieneAsistenciaHoy) {
                Asistencia falta = Asistencia.builder()
                        .estudiante(estudiante)
                        .fecha(hoy)
                        .horaRegistro(null)
                        .estadoAsistencia(EstadoAsistencia.FALTA)
                        .registradoPor(null)
                        .build();
                falta.setUsrCreacion("SCHEDULER");
                asistenciaRepository.save(falta);
                faltasMarcadas++;
            }
        }

        log.info("Faltas marcadas automáticamente: {}", faltasMarcadas);
    }
}
