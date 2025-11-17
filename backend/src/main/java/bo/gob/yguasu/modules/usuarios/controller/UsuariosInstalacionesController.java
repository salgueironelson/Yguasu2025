package bo.gob.yguasu.modules.usuarios.controller;

import bo.gob.yguasu.common.dto.ApiResponse;
import bo.gob.yguasu.modules.usuarios.dto.*;
import bo.gob.yguasu.modules.usuarios.service.UsuariosInstalacionesService;
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
 * Controller REST para gestión de usuarios e instalaciones
 */
@RestController
@RequestMapping("/api/usuarios-instalaciones")
@RequiredArgsConstructor
@Tag(name = "Usuarios e Instalaciones", description = "API para gestión de usuarios/clientes y sus instalaciones")
public class UsuariosInstalacionesController {

    private final UsuariosInstalacionesService service;

    /**
     * Crea un nuevo usuario/cliente
     */
    @PostMapping("/usuarios")
    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario/cliente en el sistema")
    public ResponseEntity<ApiResponse<UsuarioDTO>> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        try {
            UsuarioDTO usuario = service.crearUsuario(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<UsuarioDTO>builder()
                            .success(true)
                            .message("Usuario creado exitosamente")
                            .data(usuario)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<UsuarioDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Crea una nueva instalación
     */
    @PostMapping("/instalaciones")
    @Operation(summary = "Crear instalación", description = "Registra una nueva instalación para un usuario")
    public ResponseEntity<ApiResponse<InstalacionDTO>> crearInstalacion(@Valid @RequestBody InstalacionCreateDTO dto) {
        try {
            InstalacionDTO instalacion = service.crearInstalacion(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.<InstalacionDTO>builder()
                            .success(true)
                            .message("Instalación creada exitosamente")
                            .data(instalacion)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<InstalacionDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Lista todos los usuarios activos
     */
    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios activos")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> listarUsuarios() {
        List<UsuarioDTO> usuarios = service.listarUsuarios();

        return ResponseEntity.ok(
                ApiResponse.<List<UsuarioDTO>>builder()
                        .success(true)
                        .message("Usuarios obtenidos exitosamente")
                        .data(usuarios)
                        .build()
        );
    }

    /**
     * Busca usuarios por término
     */
    @GetMapping("/usuarios/buscar")
    @Operation(summary = "Buscar usuarios", description = "Busca usuarios por nombre, CI o NIT")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> buscarUsuarios(
            @Parameter(description = "Término de búsqueda", example = "Juan")
            @RequestParam String busqueda) {

        List<UsuarioDTO> usuarios = service.buscarUsuarios(busqueda);

        return ResponseEntity.ok(
                ApiResponse.<List<UsuarioDTO>>builder()
                        .success(true)
                        .message("Búsqueda completada. Se encontraron " + usuarios.size() + " usuarios")
                        .data(usuarios)
                        .build()
        );
    }

    /**
     * Obtiene un usuario con todas sus instalaciones
     */
    @GetMapping("/usuarios/{idUsuario}/completo")
    @Operation(summary = "Obtener usuario completo", description = "Obtiene un usuario con todas sus instalaciones")
    public ResponseEntity<ApiResponse<UsuarioConInstalacionesDTO>> obtenerUsuarioCompleto(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Integer idUsuario) {

        try {
            UsuarioConInstalacionesDTO usuarioCompleto = service.obtenerUsuarioConInstalaciones(idUsuario);

            return ResponseEntity.ok(
                    ApiResponse.<UsuarioConInstalacionesDTO>builder()
                            .success(true)
                            .message("Usuario obtenido exitosamente")
                            .data(usuarioCompleto)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<UsuarioConInstalacionesDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    /**
     * Lista instalaciones de un usuario
     */
    @GetMapping("/usuarios/{idUsuario}/instalaciones")
    @Operation(summary = "Listar instalaciones de usuario", description = "Obtiene todas las instalaciones de un usuario")
    public ResponseEntity<ApiResponse<List<InstalacionDTO>>> listarInstalacionesPorUsuario(
            @Parameter(description = "ID del usuario", example = "1")
            @PathVariable Integer idUsuario) {

        try {
            List<InstalacionDTO> instalaciones = service.listarInstalacionesPorUsuario(idUsuario);

            return ResponseEntity.ok(
                    ApiResponse.<List<InstalacionDTO>>builder()
                            .success(true)
                            .message("Instalaciones obtenidas exitosamente")
                            .data(instalaciones)
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<List<InstalacionDTO>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
