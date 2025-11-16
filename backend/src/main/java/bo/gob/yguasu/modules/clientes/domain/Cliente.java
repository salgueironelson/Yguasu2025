package bo.gob.yguasu.modules.clientes.domain;

import bo.gob.yguasu.common.domain.AuditableEntity;
import bo.gob.yguasu.common.domain.EstadoCivil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cnt_usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_civil", nullable = false)
    private EstadoCivil estadoCivil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "id_doc_departamento")
    private Integer idDocDepartamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(name = "paterno", nullable = false)
    private String paterno;

    @Column(name = "materno", nullable = false)
    private String materno;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "sexo", length = 1, nullable = false)
    private Character sexo;

    @Column(name = "ci")
    private String ci;

    @Column(name = "nit")
    private String nit;

    @Column(name = "fec_nacimiento")
    private LocalDate fecNacimiento;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "fax")
    private String fax;

    @Column(name = "celular")
    private String celular;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "cod_anterior")
    private Integer codAnterior;

    @PrePersist
    @PreUpdate
    public void calcularNombreCompleto() {
        this.nombreCompleto = String.format("%s %s %s", paterno, materno, nombres).trim();
    }
}
