package com.lemas.asistencia.controller;

import com.lemas.asistencia.model.dto.LoginRequest;
import com.lemas.asistencia.model.dto.LoginResponse;
import com.lemas.asistencia.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // JWT es stateless, el logout se maneja en el cliente eliminando el token
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada exitosamente"));
    }

    @PostMapping("/fcm-token")
    public ResponseEntity<Map<String, String>> updateFcmToken(@RequestBody Map<String, String> request) {
        String fcmToken = request.get("fcmToken");
        Long usuarioId = authService.getCurrentUser().getId();
        authService.updateFcmToken(usuarioId, fcmToken);
        return ResponseEntity.ok(Map.of("message", "Token FCM actualizado"));
    }
}
