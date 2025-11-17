package bo.gob.yguasu.modules.usuarios.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para crear un nuevo usuario/cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO {

    @NotBlank(message = "El apellido paterno es requerido")
    @Size(max = 100, message = "El apellido paterno no puede exceder 100 caracteres")
    private String paterno;

    @Size(max = 100, message = "El apellido materno no puede exceder 100 caracteres")
    private String materno;

    @NotBlank(message = "Los nombres son requeridos")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;

    @NotNull(message = "El sexo es requerido")
    @Pattern(regexp = "[MF]", message = "El sexo debe ser M o F")
    private String sexo;

    @NotBlank(message = "El CI es requerido")
    @Size(max = 20, message = "El CI no puede exceder 20 caracteres")
    private String ci;

    @Size(max = 20, message = "El NIT no puede exceder 20 caracteres")
    private String nit;

    private LocalDate fecNacimiento;

    @NotBlank(message = "La dirección es requerida")
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @Size(max = 20, message = "El celular no puede exceder 20 caracteres")
    private String celular;

    @NotNull(message = "El ID de estado civil es requerido")
    private Integer idEstadoCivil;

    @NotNull(message = "El ID de tipo de usuario es requerido")
    private Integer idTipoUsuario;

    private Integer idTipoDocumento;

    private Integer idDocDepartamento;
}
