package bo.gob.yguasu.modules.servicios.controller;

import bo.gob.yguasu.modules.servicios.dto.*;
import bo.gob.yguasu.modules.servicios.service.ServiciosInstalacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de servicios de instalaciones
 */
@RestController
@RequestMapping("/api/servicios-instalacion")
@RequiredArgsConstructor
@Tag(name = "Servicios de Instalación", description = "Endpoints para gestión de servicios asignados a instalaciones")
public class ServiciosInstalacionController {

    private final ServiciosInstalacionService serviciosInstalacionService;

    /**
     * Lista servicios disponibles para asignar (tipo servicio = 1)
     */
    @GetMapping("/servicios-disponibles")
    @Operation(summary = "Listar servicios disponibles",
               description = "Obtiene todos los servicios disponibles para asignar a instalaciones (tipo servicio = 1)")
    public ResponseEntity<List<ServicioDTO>> listarServiciosDisponibles() {
        List<ServicioDTO> servicios = serviciosInstalacionService.listarServiciosDisponibles();
        return ResponseEntity.ok(servicios);
    }

    /**
     * Lista servicios asignados a una instalación
     */
    @GetMapping("/instalacion/{idInstalacion}")
    @Operation(summary = "Listar servicios de instalación",
               description = "Obtiene todos los servicios asignados a una instalación específica")
    public ResponseEntity<List<InstalacionServicioDTO>> listarServiciosPorInstalacion(
            @PathVariable Integer idInstalacion) {
        List<InstalacionServicioDTO> servicios =
                serviciosInstalacionService.listarServiciosPorInstalacion(idInstalacion);
        return ResponseEntity.ok(servicios);
    }

    /**
     * Lista medidores disponibles
     */
    @GetMapping("/medidores-disponibles")
    @Operation(summary = "Listar medidores disponibles",
               description = "Obtiene todos los medidores que no están asignados a ningún servicio")
    public ResponseEntity<List<MedidorDTO>> listarMedidoresDisponibles() {
        List<MedidorDTO> medidores = serviciosInstalacionService.listarMedidoresDisponibles();
        return ResponseEntity.ok(medidores);
    }

    /**
     * Asigna un servicio a una instalación
     */
    @PostMapping
    @Operation(summary = "Asignar servicio",
               description = "Asigna un servicio a una instalación, opcionalmente con un medidor")
    public ResponseEntity<InstalacionServicioDTO> asignarServicio(
            @Valid @RequestBody AsignarServicioDTO dto) {
        InstalacionServicioDTO resultado = serviciosInstalacionService.asignarServicio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    /**
     * Desactiva un servicio de una instalación
     */
    @DeleteMapping("/{idInstalacionServicio}")
    @Operation(summary = "Desactivar servicio",
               description = "Desactiva un servicio asignado a una instalación")
    public ResponseEntity<Void> desactivarServicio(@PathVariable Integer idInstalacionServicio) {
        serviciosInstalacionService.desactivarServicio(idInstalacionServicio);
        return ResponseEntity.noContent().build();
    }
}
