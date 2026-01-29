package com.lemas.asistencia.service;

import com.lemas.asistencia.model.dto.LoginRequest;
import com.lemas.asistencia.model.dto.LoginResponse;
import com.lemas.asistencia.model.dto.UsuarioResponse;
import com.lemas.asistencia.repository.UsuarioRepository;
import com.lemas.asistencia.security.JwtTokenProvider;
import com.lemas.asistencia.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCedula(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return LoginResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(UsuarioResponse.builder()
                        .id(userPrincipal.getId())
                        .cedula(userPrincipal.getCedula())
                        .nombre(userPrincipal.getNombre())
                        .rol(userPrincipal.getRol())
                        .build())
                .build();
    }

    @Transactional
    public void updateFcmToken(Long usuarioId, String fcmToken) {
        usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
            usuario.setFcmToken(fcmToken);
            usuarioRepository.save(usuario);
        });
    }

    public UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }
}
