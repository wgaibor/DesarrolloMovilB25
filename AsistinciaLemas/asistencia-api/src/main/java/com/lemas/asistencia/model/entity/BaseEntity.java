package com.lemas.asistencia.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @Column(name = "estado", length = 1)
    private String estado = "A";

    @CreatedBy
    @Column(name = "usr_creacion", length = 50, updatable = false)
    private String usrCreacion;

    @CreatedDate
    @Column(name = "fe_creacion", updatable = false)
    private LocalDateTime feCreacion;

    @LastModifiedBy
    @Column(name = "usr_modificacion", length = 50)
    private String usrModificacion;

    @LastModifiedDate
    @Column(name = "fe_ult_modificacion")
    private LocalDateTime feUltModificacion;

    public boolean isActivo() {
        return "A".equals(this.estado);
    }

    public void activar() {
        this.estado = "A";
    }

    public void inactivar() {
        this.estado = "I";
    }
}
