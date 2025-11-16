package bo.gob.yguasu.modules.reclamos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDTO {

    @NotNull(message = "El usuario destino es requerido")
    private Integer usuarioDestino;

    private String comentario;
}
