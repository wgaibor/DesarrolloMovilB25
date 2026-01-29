package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.RepresentanteEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepresentanteEstudianteRepository extends JpaRepository<RepresentanteEstudiante, Long> {
    List<RepresentanteEstudiante> findByRepresentanteIdAndEstado(Long representanteId, String estado);
    List<RepresentanteEstudiante> findByEstudianteIdAndEstado(Long estudianteId, String estado);
}
