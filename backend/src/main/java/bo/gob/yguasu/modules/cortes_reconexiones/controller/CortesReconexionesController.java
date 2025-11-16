package bo.gob.yguasu.modules.cortes_reconexiones.controller;

import bo.gob.yguasu.common.dto.ApiResponse;
import bo.gob.yguasu.modules.cortes_reconexiones.dto.*;
import bo.gob.yguasu.modules.cortes_reconexiones.service.CortesReconexionesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de cortes y reconexiones de servicio
 */
@RestController
@RequestMapping("/api/cortes-reconexiones")
@RequiredArgsConstructor
@Tag(name = "Cortes y Reconexiones", description = "API para gestión de cortes y reconexiones de servicio")
public class CortesReconexionesController {

    private final CortesReconexionesService cortesReconexionesService;

    /**
     * Corta el servicio de una instalación
     */
    @PostMapping("/cortes")
    @Operation(summary = "Cortar servicio",
               description = "Registra un corte y actualiza el estado de la instalación a cortado")
    public ResponseEntity<ApiResponse<CorteDTO>> cortarServicio(
            @Valid @RequestBody CorteCreateDTO dto) {

        try {
            // TODO: Obtener ID de usuario del contexto de seguridad
            Integer idUsuario = 1; // Temporal

            CorteDTO corte = cortesReconexionesService.cortarServicio(dto, idUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<CorteDTO>builder()
                            .success(true)
                            .message("Servicio cortado exitosamente")
                            .data(corte)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<CorteDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Reconecta el servicio de una instalación
     */
    @PostMapping("/reconexiones")
    @Operation(summary = "Reconectar servicio",
               description = "Registra una reconexión y actualiza el estado de la instalación a activo")
    public ResponseEntity<ApiResponse<ReconexionDTO>> reconectarServicio(
            @Valid @RequestBody ReconexionCreateDTO dto) {

        try {
            // TODO: Obtener ID de usuario del contexto de seguridad
            Integer idUsuario = 1; // Temporal

            ReconexionDTO reconexion = cortesReconexionesService.reconectarServicio(dto, idUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<ReconexionDTO>builder()
                            .success(true)
                            .message("Servicio reconectado exitosamente")
                            .data(reconexion)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ReconexionDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Obtiene el historial completo de cortes y reconexiones de una instalación
     */
    @GetMapping("/historial/{codigoInstalacion}")
    @Operation(summary = "Obtener historial de cortes y reconexiones",
               description = "Obtiene el historial completo de cortes y reconexiones de una instalación")
    public ResponseEntity<ApiResponse<HistorialCortesReconexionesDTO>> obtenerHistorial(
            @Parameter(description = "Código de instalación", example = "12345")
            @PathVariable Integer codigoInstalacion) {

        try {
            HistorialCortesReconexionesDTO historial = cortesReconexionesService.obtenerHistorial(codigoInstalacion);

            return ResponseEntity.ok(
                    ApiResponse.<HistorialCortesReconexionesDTO>builder()
                            .success(true)
                            .message("Historial obtenido exitosamente")
                            .data(historial)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<HistorialCortesReconexionesDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Lista todos los cortes registrados
     */
    @GetMapping("/cortes")
    @Operation(summary = "Listar todos los cortes",
               description = "Obtiene el listado completo de cortes registrados")
    public ResponseEntity<ApiResponse<List<CorteDTO>>> listarCortes() {
        List<CorteDTO> cortes = cortesReconexionesService.listarCortes();

        return ResponseEntity.ok(
                ApiResponse.<List<CorteDTO>>builder()
                        .success(true)
                        .message("Cortes obtenidos exitosamente")
                        .data(cortes)
                        .build()
        );
    }

    /**
     * Lista todas las reconexiones registradas
     */
    @GetMapping("/reconexiones")
    @Operation(summary = "Listar todas las reconexiones",
               description = "Obtiene el listado completo de reconexiones registradas")
    public ResponseEntity<ApiResponse<List<ReconexionDTO>>> listarReconexiones() {
        List<ReconexionDTO> reconexiones = cortesReconexionesService.listarReconexiones();

        return ResponseEntity.ok(
                ApiResponse.<List<ReconexionDTO>>builder()
                        .success(true)
                        .message("Reconexiones obtenidas exitosamente")
                        .data(reconexiones)
                        .build()
        );
    }
}
