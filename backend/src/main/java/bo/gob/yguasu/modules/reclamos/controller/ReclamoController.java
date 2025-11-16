package bo.gob.yguasu.modules.reclamos.controller;

import bo.gob.yguasu.common.dto.ApiResponse;
import bo.gob.yguasu.modules.reclamos.dto.*;
import bo.gob.yguasu.modules.reclamos.service.ReclamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reclamos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reclamos", description = "Gestión de reclamos de clientes")
public class ReclamoController {

    private final ReclamoService reclamoService;

    @PostMapping
    @Operation(summary = "Crear reclamo", description = "Registrar un nuevo reclamo (con o sin instalación)")
    public ResponseEntity<ApiResponse<ReclamoDTO>> crear(@Valid @RequestBody ReclamoCreateDTO dto) {
        // TODO: Obtener idUsuario del contexto de seguridad
        ReclamoDTO reclamo = reclamoService.crearReclamo(dto, 1);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reclamo creado exitosamente. Número: " + reclamo.getNumero(), reclamo));
    }

    @GetMapping
    @Operation(summary = "Listar reclamos", description = "Obtener lista de reclamos con filtros opcionales")
    public ResponseEntity<ApiResponse<Page<ReclamoDTO>>> listar(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String tipoReclamo,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String procedente,
            @RequestParam(required = false) Integer usuarioActual,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta,
            Pageable pageable
    ) {
        ReclamoFilterDTO filtros = ReclamoFilterDTO.builder()
                .busqueda(busqueda)
                .tipoReclamo(tipoReclamo)
                .estado(estado)
                .departamento(departamento)
                .procedente(procedente)
                .usuarioActual(usuarioActual)
                .fechaDesde(fechaDesde)
                .fechaHasta(fechaHasta)
                .build();

        Page<ReclamoDTO> reclamos = reclamoService.listarConFiltros(filtros, pageable);
        return ResponseEntity.ok(ApiResponse.success(reclamos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reclamo", description = "Obtener detalles de un reclamo específico")
    public ResponseEntity<ApiResponse<ReclamoDTO>> obtenerPorId(@PathVariable Integer id) {
        ReclamoDTO reclamo = reclamoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(reclamo));
    }

    @GetMapping("/{id}/historial")
    @Operation(summary = "Obtener historial", description = "Obtener historial de pasos de un reclamo")
    public ResponseEntity<ApiResponse<List<PasoDTO>>> obtenerHistorial(@PathVariable Integer id) {
        List<PasoDTO> historial = reclamoService.obtenerHistorial(id);
        return ResponseEntity.ok(ApiResponse.success(historial));
    }

    @PostMapping("/{id}/comentario")
    @Operation(summary = "Agregar comentario", description = "Registrar un comentario/avance en el reclamo")
    public ResponseEntity<ApiResponse<ReclamoDTO>> agregarComentario(
            @PathVariable Integer id,
            @Valid @RequestBody ComentarioDTO dto
    ) {
        // TODO: Obtener idUsuario del contexto de seguridad
        ReclamoDTO reclamo = reclamoService.agregarComentario(id, dto, 1);
        return ResponseEntity.ok(ApiResponse.success("Comentario agregado exitosamente", reclamo));
    }

    @PostMapping("/{id}/transferir")
    @Operation(summary = "Transferir reclamo", description = "Transferir reclamo a otro usuario/área")
    public ResponseEntity<ApiResponse<ReclamoDTO>> transferir(
            @PathVariable Integer id,
            @Valid @RequestBody TransferenciaDTO dto
    ) {
        // TODO: Obtener idUsuario del contexto de seguridad
        ReclamoDTO reclamo = reclamoService.transferirReclamo(id, dto, 1);
        return ResponseEntity.ok(ApiResponse.success("Reclamo transferido exitosamente", reclamo));
    }

    @PostMapping("/{id}/concluir")
    @Operation(summary = "Concluir reclamo", description = "Cerrar/finalizar un reclamo")
    public ResponseEntity<ApiResponse<ReclamoDTO>> concluir(
            @PathVariable Integer id,
            @Valid @RequestBody ConclusionDTO dto
    ) {
        // TODO: Obtener idUsuario del contexto de seguridad
        ReclamoDTO reclamo = reclamoService.concluirReclamo(id, dto, 1);
        return ResponseEntity.ok(ApiResponse.success("Reclamo concluido exitosamente", reclamo));
    }
}
