package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuarioIdAndEstado(Long usuarioId, String estado);
    Optional<Estudiante> findByCodigoQrAndEstado(String codigoQr, String estado);
    List<Estudiante> findByCursoIdAndEstado(Long cursoId, String estado);

    @Query("SELECT e FROM Estudiante e WHERE e.curso.id = :cursoId AND e.estado = 'A' " +
           "AND e.id NOT IN (SELECT a.estudiante.id FROM Asistencia a WHERE a.fecha = CURRENT_DATE)")
    List<Estudiante> findEstudiantesSinAsistenciaHoy(@Param("cursoId") Long cursoId);
}
