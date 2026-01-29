package com.lemas.asistencia.service;

import com.lemas.asistencia.model.entity.Estudiante;
import com.lemas.asistencia.model.entity.RepresentanteEstudiante;
import com.lemas.asistencia.repository.RepresentanteEstudianteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final RepresentanteEstudianteRepository representanteEstudianteRepository;

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    public void notificarAtraso(Estudiante estudiante) {
        if (!firebaseEnabled) {
            log.info("Firebase deshabilitado. Atraso de {} no notificado.", estudiante.getUsuario().getNombre());
            return;
        }

        List<RepresentanteEstudiante> relaciones =
                representanteEstudianteRepository.findByEstudianteIdAndEstado(estudiante.getId(), "A");

        for (RepresentanteEstudiante relacion : relaciones) {
            String fcmToken = relacion.getRepresentante().getUsuario().getFcmToken();
            if (fcmToken != null && !fcmToken.isEmpty()) {
                enviarPushNotification(
                        fcmToken,
                        "Atraso registrado",
                        estudiante.getUsuario().getNombre() + " llegó tarde hoy."
                );
            }
        }
    }

    private void enviarPushNotification(String token, String title, String body) {
        // TODO: Implementar con Firebase Admin SDK cuando se configure
        log.info("Push notification: token={}, title={}, body={}", token, title, body);
    }
}
