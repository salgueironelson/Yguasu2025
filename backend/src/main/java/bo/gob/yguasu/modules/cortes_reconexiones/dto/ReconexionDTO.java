package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de reconexión
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReconexionDTO {

    private Integer idReconexion;
    private Integer idInstalacion;
    private Integer codigoInstalacion;
    private String nombreCliente;
    private LocalDateTime fechaReconexion;
    private String motivo;
    private String observaciones;
    private String usuarioRegistro;
    private String estado;
}
