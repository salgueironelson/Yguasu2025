package bo.gob.yguasu.modules.reclamos.domain;

import bo.gob.yguasu.modules.clientes.domain.Cliente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_reclamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "codigo_instalacion")
    private Integer codigoInstalacion;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "tipo_reclamo", length = 100)
    private String tipoReclamo;

    @Column(name = "departamento", length = 100)
    private String departamento;

    @Column(name = "reclamante", length = 100)
    private String reclamante;

    @Column(name = "detalle", length = 500)
    private String detalle;

    @Column(name = "fecha_reclamo")
    private LocalDateTime fechaReclamo;

    @Column(name = "fecha_solucion")
    private LocalDateTime fechaSolucion;

    @Column(name = "foto", length = 100)
    private String foto;

    @Column(name = "estado", length = 50)
    private String estado; // PENDIENTE, EN_PROCESO, TRANSFERIDO, CONCLUIDO

    @Column(name = "conclusion", length = 500)
    private String conclusion;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Column(name = "usuario_actual")
    private Integer usuarioActual;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "usuario_registro")
    private Integer usuarioRegistro;

    @Column(name = "procedente", length = 10)
    private String procedente; // SI, NO, null (cuando no se ha concluido)

    @OneToMany(mappedBy = "reclamo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Paso> pasos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (fechaReclamo == null) {
            fechaReclamo = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "PENDIENTE";
        }
    }
}
