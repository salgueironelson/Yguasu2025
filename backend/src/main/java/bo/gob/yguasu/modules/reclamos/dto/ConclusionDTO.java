package bo.gob.yguasu.modules.reclamos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConclusionDTO {

    @NotNull(message = "Debe indicar si el reclamo fue procedente")
    private Boolean procedente;

    @NotBlank(message = "La explicación de la conclusión es requerida")
    @Size(min = 20, message = "La explicación debe tener al menos 20 caracteres")
    private String conclusion;
}
