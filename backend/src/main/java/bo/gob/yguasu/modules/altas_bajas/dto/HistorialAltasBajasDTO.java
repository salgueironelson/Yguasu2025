package bo.gob.yguasu.modules.altas_bajas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para el historial completo de altas y bajas de una instalación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialAltasBajasDTO {

    private Integer codigoInstalacion;
    private String nombreCliente;
    private String estadoActual;
    private List<BajaDTO> bajas;
    private List<AltaDTO> altas;
}
