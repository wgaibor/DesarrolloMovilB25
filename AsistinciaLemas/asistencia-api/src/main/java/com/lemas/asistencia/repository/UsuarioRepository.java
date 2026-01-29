package com.lemas.asistencia.repository;

import com.lemas.asistencia.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCedulaAndEstado(String cedula, String estado);
    Optional<Usuario> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
}
