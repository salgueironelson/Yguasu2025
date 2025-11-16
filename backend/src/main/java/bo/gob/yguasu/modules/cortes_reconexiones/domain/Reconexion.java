package bo.gob.yguasu.modules.cortes_reconexiones.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad para la tabla com_reconexiones
 * Representa el registro de una reconexión de servicio
 */
@Entity
@Table(name = "com_reconexiones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reconexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reconexion")
    private Integer idReconexion;

    @Column(name = "id_instalacion", nullable = false)
    private Integer idInstalacion;

    @Column(name = "fecha_reconexion", nullable = false)
    private LocalDateTime fechaReconexion;

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
        if (fechaReconexion == null) {
            fechaReconexion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "A";
        }
    }
}
