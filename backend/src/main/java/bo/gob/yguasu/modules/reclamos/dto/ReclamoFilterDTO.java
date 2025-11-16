package bo.gob.yguasu.modules.reclamos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReclamoFilterDTO {

    private String busqueda; // Código, nombre, medidor
    private String tipoReclamo;
    private String estado;
    private String departamento;
    private String procedente;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private Integer usuarioActual;
}
