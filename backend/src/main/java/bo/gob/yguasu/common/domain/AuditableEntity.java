package bo.gob.yguasu.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {

    @CreatedDate
    @Column(name = "_registrado", nullable = false, updatable = false)
    private LocalDateTime registrado;

    @LastModifiedDate
    @Column(name = "_modificado")
    private LocalDateTime modificado;

    @Column(name = "_id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "_estado", length = 1, nullable = false)
    private Character estado = 'A';

    @PrePersist
    protected void onCreate() {
        if (registrado == null) {
            registrado = LocalDateTime.now();
        }
        if (estado == null) {
            estado = 'A';
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modificado = LocalDateTime.now();
    }
}
