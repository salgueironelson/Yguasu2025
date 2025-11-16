package bo.gob.yguasu.modules.instalaciones.controller;

import bo.gob.yguasu.common.dto.ApiResponse;
import bo.gob.yguasu.modules.instalaciones.dto.InstalacionDataDTO;
import bo.gob.yguasu.modules.instalaciones.service.InstalacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión de instalaciones
 */
@RestController
@RequestMapping("/api/instalaciones")
@RequiredArgsConstructor
@Tag(name = "Instalaciones", description = "API para gestión de instalaciones de servicio")
public class InstalacionController {

    private final InstalacionService instalacionService;

    /**
     * Busca los datos completos de una instalación por su código
     * Usado principalmente para autocompletar datos al crear reclamos
     *
     * @param codigo Código de instalación (ej: 12345)
     * @return Datos completos de la instalación
     */
    @GetMapping("/buscar/{codigo}")
    @Operation(summary = "Buscar instalación por código",
               description = "Obtiene los datos completos de una instalación incluyendo cliente, categoría, catastro y medidor")
    public ResponseEntity<ApiResponse<InstalacionDataDTO>> buscarPorCodigo(
            @Parameter(description = "Código de instalación", example = "12345")
            @PathVariable Integer codigo) {

        return instalacionService.buscarPorCodigo(codigo)
                .map(data -> ResponseEntity.ok(
                        ApiResponse.<InstalacionDataDTO>builder()
                                .success(true)
                                .message("Instalación encontrada")
                                .data(data)
                                .build()
                ))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<InstalacionDataDTO>builder()
                                .success(false)
                                .message("No se encontró instalación con el código: " + codigo)
                                .build()
                        ));
    }
}
