package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String nivel;

    @Column(nullable = false, length = 5)
    private String paralelo;

    @Column(name = "anio_lectivo", nullable = false, length = 9)
    private String anioLectivo;

    public String getNombreCompleto() {
        return nombre + " " + paralelo;
    }
}
