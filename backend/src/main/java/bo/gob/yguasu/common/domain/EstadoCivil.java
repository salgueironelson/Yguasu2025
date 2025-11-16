package bo.gob.yguasu.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_bp_estados_civiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoCivil extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_civil")
    private Integer idEstadoCivil;

    @Column(name = "estado_civil", nullable = false)
    private String estadoCivil;
}
