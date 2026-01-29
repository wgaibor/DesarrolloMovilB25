package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByUsuarioIdAndEstado(Long usuarioId, String estado);
}
