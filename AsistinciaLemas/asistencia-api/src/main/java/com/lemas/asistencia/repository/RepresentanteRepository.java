package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Representante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepresentanteRepository extends JpaRepository<Representante, Long> {
    Optional<Representante> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
