package bo.gob.yguasu.modules.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para respuesta de usuario/cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Integer idUsuario;
    private String paterno;
    private String materno;
    private String nombres;
    private String nombreCompleto;
    private String sexo;
    private String ci;
    private String nit;
    private LocalDate fecNacimiento;
    private String direccion;
    private String telefono;
    private String celular;
    private Integer idEstadoCivil;
    private String estadoCivil;
    private Integer idTipoUsuario;
    private String tipoUsuario;
    private Integer totalInstalaciones;
}
