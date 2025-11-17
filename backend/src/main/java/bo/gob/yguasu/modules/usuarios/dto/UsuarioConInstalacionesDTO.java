package bo.gob.yguasu.modules.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para usuario con sus instalaciones
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioConInstalacionesDTO {
    private UsuarioDTO usuario;
    private List<InstalacionDTO> instalaciones;
    private Integer totalInstalaciones;
    private Integer instalacionesActivas;
    private Integer instalacionesCortadas;
    private Integer instalacionesInactivas;
}
