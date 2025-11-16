package bo.gob.yguasu.modules.clientes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDTO {

    private Integer idUsuario;

    @NotNull(message = "El estado civil es requerido")
    private Integer idEstadoCivil;
    private String estadoCivil;

    @NotNull(message = "El tipo de usuario es requerido")
    private Integer idTipoUsuario;
    private String tipoUsuario;

    private Integer idDocDepartamento;
    private Integer idTipoDocumento;
    private String tipoDocumento;

    @NotBlank(message = "El apellido paterno es requerido")
    private String paterno;

    @NotBlank(message = "El apellido materno es requerido")
    private String materno;

    @NotBlank(message = "Los nombres son requeridos")
    private String nombres;

    @NotNull(message = "El sexo es requerido")
    private Character sexo;

    private String ci;
    private String nit;
    private LocalDate fecNacimiento;
    private String direccion;
    private String telefono;
    private String fax;
    private String celular;
    private String nombreCompleto;
    private Integer codAnterior;
}
