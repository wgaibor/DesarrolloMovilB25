package com.lemas.asistencia.security;

import com.lemas.asistencia.model.entity.Usuario;
import com.lemas.asistencia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCedulaAndEstado(cedula, "A")
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + cedula));
        return UserPrincipal.create(usuario);
    }
}
