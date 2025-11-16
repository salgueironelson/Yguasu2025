package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para filtros de búsqueda de instalaciones para corte masivo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiltrosCorteMasivoDTO {
    private String zonaInicial;
    private String zonaFinal;
    private Integer cantidadFacturas;
}
