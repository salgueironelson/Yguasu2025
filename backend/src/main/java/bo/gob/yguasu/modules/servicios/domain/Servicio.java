package bo.gob.yguasu.modules.servicios.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad para la tabla com_servicios
 * Representa los servicios que se pueden asignar a instalaciones
 */
@Entity
@Table(name = "com_servicios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(name = "id_tipo_servicio")
    private Integer idTipoServicio;

    @Column(name = "servicio", length = 100)
    private String servicio;

    @Column(name = "_activo")
    private Boolean activo;
}
