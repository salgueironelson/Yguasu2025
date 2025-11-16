package bo.gob.yguasu.modules.reclamos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "usuario", length = 100)
    private String usuario;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "departamento", length = 100)
    private String departamento;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "fecha_registro", length = 20)
    private String fechaRegistro;
}
