package bo.gob.yguasu.modules.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para servicio disponible
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Integer idServicio;
    private String servicio;
    private Integer idTipoServicio;
}
