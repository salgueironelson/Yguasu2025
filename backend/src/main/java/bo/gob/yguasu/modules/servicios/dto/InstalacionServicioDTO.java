package bo.gob.yguasu.modules.servicios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para servicios asignados a una instalación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstalacionServicioDTO {
    private Integer idInstalacionServicio;
    private Integer idInstalacion;
    private Integer idServicio;
    private String nombreServicio;
    private Integer idMedidor;
    private String serieMedidor;
    private String estado;
    private String estadoDescripcion;
}
