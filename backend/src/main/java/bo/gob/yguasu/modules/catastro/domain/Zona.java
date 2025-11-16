package bo.gob.yguasu.modules.catastro.domain;

import bo.gob.yguasu.common.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tcn_zonas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zona extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private Integer idZona;

    @Column(name = "zona", nullable = false)
    private Integer zona;

    @Column(name = "m3_plano")
    private Integer m3Plano;

    @Column(name = "monto_plano", precision = 10, scale = 2)
    private BigDecimal montoPlano;

    @Column(name = "monto_m3", precision = 10, scale = 2)
    private BigDecimal montoM3;

    @Column(name = "monto_form", precision = 10, scale = 2)
    private BigDecimal montoForm;
}
