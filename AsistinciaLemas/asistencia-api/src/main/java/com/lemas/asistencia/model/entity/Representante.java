package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "representantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Representante extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "representante", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<RepresentanteEstudiante> estudiantes = new HashSet<>();
}
