package bo.gob.yguasu.modules.altas_bajas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad para la tabla com_altas
 * Representa el registro de un alta de instalación
 */
@Entity
@Table(name = "com_altas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alta")
    private Integer idAlta;

    @Column(name = "id_instalacion", nullable = false)
    private Integer idInstalacion;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    @Column(name = "motivo", length = 100)
    private String motivo;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "_estado", length = 1)
    private String estado;

    @PrePersist
    protected void onCreate() {
        if (fechaAlta == null) {
            fechaAlta = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "A";
        }
    }
}
