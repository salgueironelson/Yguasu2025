package bo.gob.yguasu.modules.clientes.domain;

import bo.gob.yguasu.common.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cnt_tipos_documentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDocumento extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Integer idTipoDocumento;

    @Column(name = "codigo_tipo_documento", nullable = false)
    private String codigoTipoDocumento;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;
}
