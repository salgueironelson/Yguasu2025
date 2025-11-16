package bo.gob.yguasu.modules.clientes.controller;

import bo.gob.yguasu.common.dto.ApiResponse;
import bo.gob.yguasu.modules.clientes.domain.Cliente;
import bo.gob.yguasu.modules.clientes.dto.ClienteDTO;
import bo.gob.yguasu.modules.clientes.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clientes", description = "Gestión de clientes/usuarios del servicio")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Obtener lista paginada de clientes activos")
    public ResponseEntity<ApiResponse<Page<Cliente>>> findAll(Pageable pageable) {
        Page<Cliente> clientes = clienteService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(clientes));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar clientes", description = "Buscar clientes por nombre, CI o NIT")
    public ResponseEntity<ApiResponse<Page<Cliente>>> search(
            @RequestParam String search,
            Pageable pageable
    ) {
        Page<Cliente> clientes = clienteService.search(search, pageable);
        return ResponseEntity.ok(ApiResponse.success(clientes));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente", description = "Obtener un cliente por su ID")
    public ResponseEntity<ApiResponse<Cliente>> findById(@PathVariable Integer id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(cliente));
    }

    @PostMapping
    @Operation(summary = "Crear cliente", description = "Crear un nuevo cliente")
    public ResponseEntity<ApiResponse<Cliente>> create(
            @Valid @RequestBody ClienteDTO dto
    ) {
        // TODO: Obtener idUsuario del contexto de seguridad
        Cliente cliente = clienteService.create(dto, 1);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente creado exitosamente", cliente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualizar un cliente existente")
    public ResponseEntity<ApiResponse<Cliente>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteDTO dto
    ) {
        // TODO: Obtener idUsuario del contexto de seguridad
        Cliente cliente = clienteService.update(id, dto, 1);
        return ResponseEntity.ok(ApiResponse.success("Cliente actualizado exitosamente", cliente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Eliminar (inactivar) un cliente")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        // TODO: Obtener idUsuario del contexto de seguridad
        clienteService.delete(id, 1);
        return ResponseEntity.ok(ApiResponse.success("Cliente eliminado exitosamente", null));
    }
}
