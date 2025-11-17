package bo.gob.yguasu.modules.servicios.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad para la tabla tcn_medidores
 * Representa los medidores que pueden estar asociados a servicios
 */
@Entity
@Table(name = "tcn_medidores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medidor")
    private Integer idMedidor;

    @Column(name = "serie", length = 50)
    private String serie;

    @Column(name = "id_marca")
    private Integer idMarca;

    @Column(name = "id_diametro")
    private Integer idDiametro;

    @Column(name = "_activo")
    private Boolean activo;
}
