package bo.gob.yguasu.modules.reclamos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class PasoDTO {

    private Integer id;
    private Integer paso;
    private String detalle;
    private String tipoPaso;
    private String comentario;

    private Integer usuario;
    private String nombreUsuario;

    private Integer usuarioDestino;
    private String nombreUsuarioDestino;

    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
}
