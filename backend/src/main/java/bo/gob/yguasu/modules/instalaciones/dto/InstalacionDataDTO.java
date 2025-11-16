package bo.gob.yguasu.modules.instalaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para datos completos de una instalación
 * Usado para autocompletar información al crear reclamos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstalacionDataDTO {

    private Integer idInstalacion;
    private Integer codigoInstalacion;
    private String nombreCompleto;
    private String calle;
    private String categoria;
    private String celular;
    private String catastro;
    private String medidor;
}
