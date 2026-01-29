package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parametros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parametro extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String clave;

    @Column(nullable = false, length = 100)
    private String valor;

    @Column(length = 255)
    private String descripcion;
}
