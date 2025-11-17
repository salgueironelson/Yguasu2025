package bo.gob.yguasu.modules.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de instalación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstalacionDTO {
    private Integer idInstalacion;
    private Integer codigoInstalacion;
    private Integer idUsuario;
    private String nombreCliente;
    private String direccion;
    private String zona;
    private String celular;
    private String estado;
    private String estadoDescripcion;
    private Integer idCategoria;
    private String categoriaDescripcion;
}
