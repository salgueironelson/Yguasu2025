package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO para respuesta de corte
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorteDTO {

    private Integer idCorte;
    private Integer idInstalacion;
    private Integer codigoInstalacion;
    private String nombreCliente;
    private LocalDateTime fechaCorte;
    private String motivo;
    private String observaciones;
    private String usuarioRegistro;
    private String estado;
}
