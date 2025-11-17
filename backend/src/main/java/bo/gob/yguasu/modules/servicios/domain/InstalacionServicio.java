package bo.gob.yguasu.modules.servicios.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad para la tabla com_ins_servicios
 * Representa la relación entre instalaciones y servicios asignados
 */
@Entity
@Table(name = "com_ins_servicios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstalacionServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instalacion_servicio")
    private Integer idInstalacionServicio;

    @Column(name = "id_instalacion", nullable = false)
    private Integer idInstalacion;

    @Column(name = "id_servicio", nullable = false)
    private Integer idServicio;

    @Column(name = "id_medidor")
    private Integer idMedidor;

    @Column(name = "_estado", length = 1)
    private String estado; // A = Activo, I = Inactivo, C = Cortado

    @Column(name = "_usuario_alta", length = 50)
    private String usuarioAlta;

    @Column(name = "_fec_alta")
    private LocalDateTime fechaAlta;

    @Column(name = "_usuario_modificacion", length = 50)
    private String usuarioModificacion;

    @Column(name = "_fec_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "_activo")
    private Boolean activo;
}
