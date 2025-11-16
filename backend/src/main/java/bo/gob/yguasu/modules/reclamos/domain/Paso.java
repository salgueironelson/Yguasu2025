package bo.gob.yguasu.modules.reclamos.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_paso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reclamo", referencedColumnName = "id", insertable = false, updatable = false)
    private Reclamo reclamo;

    @Column(name = "id_reclamo", length = 50)
    private String idReclamo;

    @Column(name = "codigo_instalacion")
    private Integer codigoInstalacion;

    @Column(name = "paso")
    private Integer paso;

    @Column(name = "detalle", length = 500)
    private String detalle;

    @Column(name = "tipo_paso", length = 100)
    private String tipoPaso; // CREACION, COMENTARIO, TRANSFERENCIA, CONCLUSION

    @Column(name = "usuario")
    private Integer usuario;

    @Column(name = "usuario_destino")
    private Integer usuarioDestino;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
