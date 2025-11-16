package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para instalación con información de deuda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstalacionConDeudaDTO {
    private Integer idInstalacion;
    private Integer codigoInstalacion;
    private String nombreCliente;
    private String direccion;
    private String zona;
    private Integer cantidadFacturasAdeudadas;
    private BigDecimal montoDeuda;
    private String estado;
}
