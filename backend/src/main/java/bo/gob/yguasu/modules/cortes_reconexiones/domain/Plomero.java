package bo.gob.yguasu.modules.cortes_reconexiones.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad para plomeros que realizan cortes y reconexiones
 */
@Entity
@Table(name = "com_plomeros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plomero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plomero")
    private Integer idPlomero;

    @Column(name = "nombrecompleto", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado;

    @Column(name = "_registrado")
    private LocalDateTime registrado;

    @Column(name = "_modificado")
    private LocalDateTime modificado;

    @Column(name = "_id_usuario", nullable = false)
    private Integer idUsuario;

    @PrePersist
    protected void onCreate() {
        if (registrado == null) {
            registrado = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "A";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modificado = LocalDateTime.now();
    }
}
