package bo.gob.yguasu.modules.auth.domain;

import bo.gob.yguasu.common.domain.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_bp_usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_item", nullable = false)
    private Integer idItem;

    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    @Column(name = "clave", nullable = false)
    private String clave;

    @Column(name = "tipo_usuario", length = 50)
    private String tipoUsuario;

    @Column(name = "ip")
    private String ip;

    @Column(name = "id_institucion")
    private Integer idInstitucion;
}
