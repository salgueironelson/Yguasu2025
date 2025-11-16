package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para el historial completo de cortes y reconexiones de una instalación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialCortesReconexionesDTO {

    private Integer codigoInstalacion;
    private String nombreCliente;
    private String estadoActual;
    private List<CorteDTO> cortes;
    private List<ReconexionDTO> reconexiones;
}
