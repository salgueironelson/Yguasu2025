package bo.gob.yguasu.modules.altas_bajas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de baja
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BajaDTO {

    private Integer idBaja;
    private Integer idInstalacion;
    private Integer codigoInstalacion;
    private String nombreCliente;
    private LocalDateTime fechaBaja;
    private String motivo;
    private String observaciones;
    private String usuarioRegistro;
    private String estado;
}
