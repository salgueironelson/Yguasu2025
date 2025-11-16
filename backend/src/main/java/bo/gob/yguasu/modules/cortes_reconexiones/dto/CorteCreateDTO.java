package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un corte de servicio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorteCreateDTO {

    @NotNull(message = "El código de instalación es requerido")
    private Integer codigoInstalacion;

    @NotNull(message = "El motivo es requerido")
    @Size(min = 3, max = 100, message = "El motivo debe tener entre 3 y 100 caracteres")
    private String motivo;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    private Integer idPlomero;
}
