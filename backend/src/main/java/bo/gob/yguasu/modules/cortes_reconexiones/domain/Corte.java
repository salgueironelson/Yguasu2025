package bo.gob.yguasu.modules.cortes_reconexiones.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad para la tabla com_cortes
 * Representa el registro de un corte de servicio
 */
@Entity
@Table(name = "com_cortes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Corte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corte")
    private Integer idCorte;

    @Column(name = "id_instalacion", nullable = false)
    private Integer idInstalacion;

    @Column(name = "fecha_corte", nullable = false)
    private LocalDateTime fechaCorte;

    @Column(name = "motivo", length = 100)
    private String motivo;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "id_plomero")
    private Integer idPlomero;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "_estado", length = 1)
    private String estado;

    @PrePersist
    protected void onCreate() {
        if (fechaCorte == null) {
            fechaCorte = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "A";
        }
    }
}
