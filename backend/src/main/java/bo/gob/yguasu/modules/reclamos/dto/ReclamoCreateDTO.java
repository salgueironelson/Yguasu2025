package bo.gob.yguasu.modules.reclamos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReclamoCreateDTO {

    // Para reclamos con instalación
    private Integer codigoInstalacion;

    // Campos obligatorios
    @NotBlank(message = "El número de celular es requerido")
    private String celular;

    @NotBlank(message = "El tipo de reclamo es requerido")
    private String tipoReclamo;

    @NotNull(message = "La fecha del reclamo es requerida")
    private LocalDateTime fechaReclamo;

    @NotBlank(message = "El detalle del reclamo es requerido")
    private String detalle;

    // Para reclamos sin instalación
    private String reclamante;

    // Opcionales
    private String ubicacion;
    private String departamento;
    private String foto;
}
