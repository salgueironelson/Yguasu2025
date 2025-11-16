package bo.gob.yguasu.modules.instalaciones.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad para la tabla cnt_instalaciones
 * Representa una instalación/conexión de servicio de agua
 */
@Entity
@Table(name = "cnt_instalaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instalacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instalacion")
    private Integer idInstalacion;

    @Column(name = "codigo_instalacion", unique = true)
    private Integer codigoInstalacion;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_calle")
    private Integer idCalle;

    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "id_zona")
    private Integer idZona;

    @Column(name = "id_ruta")
    private Integer idRuta;

    @Column(name = "id_manzana")
    private Integer idManzana;

    @Column(name = "id_secuencia")
    private Integer idSecuencia;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "_estado", length = 1)
    private String estado;
}
