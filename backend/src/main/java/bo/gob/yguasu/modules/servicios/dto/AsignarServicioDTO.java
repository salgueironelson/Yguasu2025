package bo.gob.yguasu.modules.servicios.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para asignar un servicio a una instalación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignarServicioDTO {

    @NotNull(message = "El ID de instalación es requerido")
    private Integer idInstalacion;

    @NotNull(message = "El ID de servicio es requerido")
    private Integer idServicio;

    private Integer idMedidor; // Opcional
}
