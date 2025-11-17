package bo.gob.yguasu.modules.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para medidor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedidorDTO {
    private Integer idMedidor;
    private String serie;
}
