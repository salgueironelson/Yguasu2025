package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para respuesta de corte masivo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorteMasivoResponseDTO {
    private Integer totalProcesados;
    private Integer totalExitosos;
    private Integer totalFallidos;
    private List<CorteDTO> cortesRealizados;
    private List<String> errores;
}
