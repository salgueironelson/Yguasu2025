package bo.gob.yguasu.modules.cortes_reconexiones.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para procesar corte masivo de instalaciones
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorteMasivoRequestDTO {

    @NotNull(message = "El ID del plomero es requerido")
    private Integer idPlomero;

    @NotEmpty(message = "Debe seleccionar al menos una instalación")
    private List<Integer> idsInstalaciones;

    @NotNull(message = "El motivo es requerido")
    private String motivo;

    private String observaciones;
}
