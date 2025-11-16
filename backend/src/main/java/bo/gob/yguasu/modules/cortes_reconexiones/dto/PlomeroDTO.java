package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para información de plomero
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlomeroDTO {
    private Integer idPlomero;
    private String nombreCompleto;
    private String dni;
    private String direccion;
    private String celular;
    private String estado;
}
