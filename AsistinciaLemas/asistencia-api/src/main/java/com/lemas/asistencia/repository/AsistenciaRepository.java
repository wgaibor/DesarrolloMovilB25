package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Asistencia;
import com.lemas.asistencia.model.enums.EstadoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    Optional<Asistencia> findByEstudianteIdAndFechaAndEstado(Long estudianteId, LocalDate fecha, String estado);

    List<Asistencia> findByEstudianteIdAndEstadoOrderByFechaDesc(Long estudianteId, String estado);

    @Query("SELECT a FROM Asistencia a WHERE a.estudiante.id = :estudianteId " +
           "AND a.estado = 'A' " +
           "AND (:estadoAsistencia IS NULL OR a.estadoAsistencia = :estadoAsistencia) " +
           "AND (:desde IS NULL OR a.fecha >= :desde) " +
           "AND (:hasta IS NULL OR a.fecha <= :hasta) " +
           "ORDER BY a.fecha DESC")
    List<Asistencia> findByEstudianteWithFilters(
            @Param("estudianteId") Long estudianteId,
            @Param("estadoAsistencia") EstadoAsistencia estadoAsistencia,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    @Query("SELECT a FROM Asistencia a WHERE a.estudiante.curso.id = :cursoId " +
           "AND a.estado = 'A' " +
           "AND (:estadoAsistencia IS NULL OR a.estadoAsistencia = :estadoAsistencia) " +
           "AND (:fecha IS NULL OR a.fecha = :fecha) " +
           "ORDER BY a.fecha DESC, a.estudiante.usuario.nombre ASC")
    List<Asistencia> findByCursoWithFilters(
            @Param("cursoId") Long cursoId,
            @Param("estadoAsistencia") EstadoAsistencia estadoAsistencia,
            @Param("fecha") LocalDate fecha);

    @Query("SELECT a FROM Asistencia a WHERE a.estudiante.id IN " +
           "(SELECT e.id FROM Estudiante e WHERE e.estado = 'A') " +
           "AND a.fecha = CURRENT_DATE AND a.estado = 'A'")
    List<Asistencia> findAsistenciasDeHoy();
}
