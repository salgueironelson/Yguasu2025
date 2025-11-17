package bo.gob.yguasu.modules.usuarios.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear una nueva instalación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstalacionCreateDTO {

    @NotNull(message = "El código de instalación es requerido")
    private Integer codigoInstalacion;

    @NotNull(message = "El ID de usuario es requerido")
    private Integer idUsuario;

    private Integer idCalle;

    @NotNull(message = "La categoría es requerida")
    private Integer idCategoria;

    private Integer idZona;

    private Integer idRuta;

    private Integer idManzana;

    private Integer idSecuencia;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;

    @Size(max = 20, message = "El celular no puede exceder 20 caracteres")
    private String celular;

    private String zona;
}
