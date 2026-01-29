package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long> {
    Optional<Parametro> findByClaveAndEstado(String clave, String estado);
}
