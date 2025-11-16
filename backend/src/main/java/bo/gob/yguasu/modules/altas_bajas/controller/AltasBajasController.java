package bo.gob.yguasu.modules.altas_bajas.controller;

import bo.gob.yguasu.common.dto.ApiResponse;
import bo.gob.yguasu.modules.altas_bajas.dto.*;
import bo.gob.yguasu.modules.altas_bajas.service.AltasBajasService;
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
 * Controlador REST para gestión de altas y bajas de instalaciones
 */
@RestController
@RequestMapping("/api/altas-bajas")
@RequiredArgsConstructor
@Tag(name = "Altas y Bajas", description = "API para gestión de altas y bajas de instalaciones")
public class AltasBajasController {

    private final AltasBajasService altasBajasService;

    /**
     * Da de baja una instalación
     */
    @PostMapping("/bajas")
    @Operation(summary = "Dar de baja una instalación",
               description = "Registra una baja y actualiza el estado de la instalación a inactivo")
    public ResponseEntity<ApiResponse<BajaDTO>> darDeBaja(
            @Valid @RequestBody BajaCreateDTO dto) {

        try {
            // TODO: Obtener ID de usuario del contexto de seguridad
            Integer idUsuario = 1; // Temporal

            BajaDTO baja = altasBajasService.darDeBaja(dto, idUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<BajaDTO>builder()
                            .success(true)
                            .message("Instalación dada de baja exitosamente")
                            .data(baja)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<BajaDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Da de alta una instalación
     */
    @PostMapping("/altas")
    @Operation(summary = "Dar de alta una instalación",
               description = "Registra un alta y actualiza el estado de la instalación a activo")
    public ResponseEntity<ApiResponse<AltaDTO>> darDeAlta(
            @Valid @RequestBody AltaCreateDTO dto) {

        try {
            // TODO: Obtener ID de usuario del contexto de seguridad
            Integer idUsuario = 1; // Temporal

            AltaDTO alta = altasBajasService.darDeAlta(dto, idUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<AltaDTO>builder()
                            .success(true)
                            .message("Instalación dada de alta exitosamente")
                            .data(alta)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<AltaDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Obtiene el historial completo de altas y bajas de una instalación
     */
    @GetMapping("/historial/{codigoInstalacion}")
    @Operation(summary = "Obtener historial de altas y bajas",
               description = "Obtiene el historial completo de altas y bajas de una instalación")
    public ResponseEntity<ApiResponse<HistorialAltasBajasDTO>> obtenerHistorial(
            @Parameter(description = "Código de instalación", example = "12345")
            @PathVariable Integer codigoInstalacion) {

        try {
            HistorialAltasBajasDTO historial = altasBajasService.obtenerHistorial(codigoInstalacion);

            return ResponseEntity.ok(
                    ApiResponse.<HistorialAltasBajasDTO>builder()
                            .success(true)
                            .message("Historial obtenido exitosamente")
                            .data(historial)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<HistorialAltasBajasDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Lista todas las bajas registradas
     */
    @GetMapping("/bajas")
    @Operation(summary = "Listar todas las bajas",
               description = "Obtiene el listado completo de bajas registradas")
    public ResponseEntity<ApiResponse<List<BajaDTO>>> listarBajas() {
        List<BajaDTO> bajas = altasBajasService.listarBajas();

        return ResponseEntity.ok(
                ApiResponse.<List<BajaDTO>>builder()
                        .success(true)
                        .message("Bajas obtenidas exitosamente")
                        .data(bajas)
                        .build()
        );
    }

    /**
     * Lista todas las altas registradas
     */
    @GetMapping("/altas")
    @Operation(summary = "Listar todas las altas",
               description = "Obtiene el listado completo de altas registradas")
    public ResponseEntity<ApiResponse<List<AltaDTO>>> listarAltas() {
        List<AltaDTO> altas = altasBajasService.listarAltas();

        return ResponseEntity.ok(
                ApiResponse.<List<AltaDTO>>builder()
                        .success(true)
                        .message("Altas obtenidas exitosamente")
                        .data(altas)
                        .build()
        );
    }
}
