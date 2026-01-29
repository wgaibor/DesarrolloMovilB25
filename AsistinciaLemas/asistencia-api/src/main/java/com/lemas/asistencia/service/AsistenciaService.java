package com.lemas.asistencia.service;

import com.lemas.asistencia.exception.BadRequestException;
import com.lemas.asistencia.exception.ResourceNotFoundException;
import com.lemas.asistencia.model.dto.*;
import com.lemas.asistencia.model.entity.*;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import com.lemas.asistencia.repository.*;
import com.lemas.asistencia.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EstudianteRepository estudianteRepository;
    private final DocenteRepository docenteRepository;
    private final RepresentanteRepository representanteRepository;
    private final RepresentanteEstudianteRepository representanteEstudianteRepository;
    private final ParametroService parametroService;
    private final NotificacionService notificacionService;
    private final AuthService authService;

    // ==================== ESTUDIANTE ====================

    @Transactional(readOnly = true)
    public QrResponse obtenerMiQr() {
        UserPrincipal user = authService.getCurrentUser();
        Estudiante estudiante = estudianteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        return QrResponse.builder()
                .codigoQr(estudiante.getCodigoQr())
                .nombreEstudiante(estudiante.getUsuario().getNombre())
                .curso(estudiante.getCurso().getNombreCompleto())
                .build();
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> obtenerMisAsistencias(EstadoAsistencia estado, LocalDate desde, LocalDate hasta) {
        UserPrincipal user = authService.getCurrentUser();
        Estudiante estudiante = estudianteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        return asistenciaRepository.findByEstudianteWithFilters(estudiante.getId(), estado, desde, hasta)
                .stream()
                .map(this::toAsistenciaResponse)
                .collect(Collectors.toList());
    }

    // ==================== DOCENTE ====================

    @Transactional(readOnly = true)
    public List<CursoResponse> obtenerMisCursos() {
        UserPrincipal user = authService.getCurrentUser();
        Docente docente = docenteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        return docente.getCursos().stream()
                .filter(c -> "A".equals(c.getEstado()))
                .map(this::toCursoResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponse> obtenerEstudiantesCurso(Long cursoId) {
        verificarDocenteTieneCurso(cursoId);

        return estudianteRepository.findByCursoIdAndEstado(cursoId, "A").stream()
                .map(e -> toEstudianteResponse(e, null))
                .collect(Collectors.toList());
    }

    @Transactional
    public AsistenciaResponse registrarAsistencia(RegistrarAsistenciaRequest request) {
        UserPrincipal user = authService.getCurrentUser();
        Docente docente = docenteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        Estudiante estudiante = estudianteRepository.findByCodigoQrAndEstado(request.getCodigoQr(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Código QR inválido"));

        // Verificar que el docente tiene asignado el curso del estudiante
        boolean tieneCurso = docente.getCursos().stream()
                .anyMatch(c -> c.getId().equals(estudiante.getCurso().getId()));
        if (!tieneCurso) {
            throw new BadRequestException("No tiene permiso para registrar asistencia de este estudiante");
        }

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        // Verificar si ya existe asistencia para hoy
        if (asistenciaRepository.findByEstudianteIdAndFechaAndEstado(estudiante.getId(), hoy, "A").isPresent()) {
            throw new BadRequestException("Ya se registró asistencia para este estudiante hoy");
        }

        // Determinar estado según la hora
        LocalTime horaLimiteAtraso = parametroService.getHoraLimiteAtraso();
        EstadoAsistencia estadoAsistencia = ahora.isBefore(horaLimiteAtraso)
                ? EstadoAsistencia.PRESENTE
                : EstadoAsistencia.ATRASO;

        Asistencia asistencia = Asistencia.builder()
                .estudiante(estudiante)
                .fecha(hoy)
                .horaRegistro(ahora)
                .estadoAsistencia(estadoAsistencia)
                .registradoPor(docente)
                .build();

        asistencia = asistenciaRepository.save(asistencia);

        // Notificar si es atraso
        if (estadoAsistencia == EstadoAsistencia.ATRASO) {
            notificacionService.notificarAtraso(estudiante);
        }

        return toAsistenciaResponse(asistencia);
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> obtenerAsistenciasCurso(Long cursoId, EstadoAsistencia estado, LocalDate fecha) {
        verificarDocenteTieneCurso(cursoId);

        return asistenciaRepository.findByCursoWithFilters(cursoId, estado, fecha).stream()
                .map(this::toAsistenciaResponse)
                .collect(Collectors.toList());
    }

    // ==================== REPRESENTANTE ====================

    @Transactional(readOnly = true)
    public List<EstudianteResponse> obtenerMisEstudiantes() {
        UserPrincipal user = authService.getCurrentUser();
        Representante representante = representanteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Representante no encontrado"));

        return representanteEstudianteRepository.findByRepresentanteIdAndEstado(representante.getId(), "A")
                .stream()
                .map(re -> toEstudianteResponse(re.getEstudiante(), re.getParentesco().name()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> obtenerAsistenciasEstudiante(Long estudianteId, EstadoAsistencia estado,
                                                                  LocalDate desde, LocalDate hasta) {
        verificarRepresentanteTieneEstudiante(estudianteId);

        return asistenciaRepository.findByEstudianteWithFilters(estudianteId, estado, desde, hasta)
                .stream()
                .map(this::toAsistenciaResponse)
                .collect(Collectors.toList());
    }

    // ==================== HELPERS ====================

    private void verificarDocenteTieneCurso(Long cursoId) {
        UserPrincipal user = authService.getCurrentUser();
        Docente docente = docenteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Docente no encontrado"));

        boolean tieneCurso = docente.getCursos().stream()
                .anyMatch(c -> c.getId().equals(cursoId));
        if (!tieneCurso) {
            throw new BadRequestException("No tiene acceso a este curso");
        }
    }

    private void verificarRepresentanteTieneEstudiante(Long estudianteId) {
        UserPrincipal user = authService.getCurrentUser();
        Representante representante = representanteRepository.findByUsuarioIdAndEstado(user.getId(), "A")
                .orElseThrow(() -> new ResourceNotFoundException("Representante no encontrado"));

        boolean tieneEstudiante = representanteEstudianteRepository
                .findByRepresentanteIdAndEstado(representante.getId(), "A")
                .stream()
                .anyMatch(re -> re.getEstudiante().getId().equals(estudianteId));

        if (!tieneEstudiante) {
            throw new BadRequestException("No tiene acceso a este estudiante");
        }
    }

    private AsistenciaResponse toAsistenciaResponse(Asistencia asistencia) {
        return AsistenciaResponse.builder()
                .id(asistencia.getId())
                .estudiante(toEstudianteResponse(asistencia.getEstudiante(), null))
                .fecha(asistencia.getFecha())
                .horaRegistro(asistencia.getHoraRegistro())
                .estadoAsistencia(asistencia.getEstadoAsistencia().name())
                .registradoPor(asistencia.getRegistradoPor() != null
                        ? toUsuarioResponse(asistencia.getRegistradoPor().getUsuario())
                        : null)
                .build();
    }

    private EstudianteResponse toEstudianteResponse(Estudiante estudiante, String parentesco) {
        return EstudianteResponse.builder()
                .id(estudiante.getId())
                .nombre(estudiante.getUsuario().getNombre())
                .cedula(estudiante.getUsuario().getCedula())
                .curso(toCursoResponse(estudiante.getCurso()))
                .parentesco(parentesco)
                .build();
    }

    private CursoResponse toCursoResponse(Curso curso) {
        return CursoResponse.builder()
                .id(curso.getId())
                .nombre(curso.getNombre())
                .nivel(curso.getNivel())
                .paralelo(curso.getParalelo())
                .anioLectivo(curso.getAnioLectivo())
                .nombreCompleto(curso.getNombreCompleto())
                .build();
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .cedula(usuario.getCedula())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }
}
