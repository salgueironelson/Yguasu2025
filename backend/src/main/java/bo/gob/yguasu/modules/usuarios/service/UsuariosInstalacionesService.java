package bo.gob.yguasu.modules.usuarios.service;

import bo.gob.yguasu.common.domain.EstadoCivil;
import bo.gob.yguasu.modules.clientes.domain.Cliente;
import bo.gob.yguasu.modules.clientes.domain.TipoUsuario;
import bo.gob.yguasu.modules.instalaciones.domain.Instalacion;
import bo.gob.yguasu.modules.usuarios.dto.*;
import bo.gob.yguasu.modules.usuarios.repository.InstalacionUsuarioRepository;
import bo.gob.yguasu.modules.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de usuarios e instalaciones
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuariosInstalacionesService {

    private final UsuarioRepository usuarioRepository;
    private final InstalacionUsuarioRepository instalacionRepository;

    /**
     * Crear nuevo usuario/cliente
     */
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioCreateDTO dto) {
        log.info("Creando nuevo usuario con CI: {}", dto.getCi());

        // Validar CI único
        if (usuarioRepository.existsByCi(dto.getCi())) {
            throw new RuntimeException("Ya existe un usuario con el CI: " + dto.getCi());
        }

        // Crear entidad
        EstadoCivil estadoCivil = new EstadoCivil();
        estadoCivil.setIdEstadoCivil(dto.getIdEstadoCivil());

        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setIdTipoUsuario(dto.getIdTipoUsuario());

        Cliente cliente = Cliente.builder()
                .paterno(dto.getPaterno())
                .materno(dto.getMaterno())
                .nombres(dto.getNombres())
                .sexo(dto.getSexo().charAt(0))
                .ci(dto.getCi())
                .nit(dto.getNit())
                .fecNacimiento(dto.getFecNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .celular(dto.getCelular())
                .estadoCivil(estadoCivil)
                .tipoUsuario(tipoUsuario)
                .build();

        // Establecer estado activo
        cliente.setEstado('A');

        cliente = usuarioRepository.save(cliente);

        log.info("Usuario creado exitosamente. ID: {}", cliente.getIdUsuario());

        return mapearClienteADTO(cliente, 0);
    }

    /**
     * Crear nueva instalación
     */
    @Transactional
    public InstalacionDTO crearInstalacion(InstalacionCreateDTO dto) {
        log.info("Creando nueva instalación para usuario ID: {}", dto.getIdUsuario());

        // Validar que el usuario existe
        Cliente usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar código único
        if (instalacionRepository.existsByCodigoInstalacion(dto.getCodigoInstalacion())) {
            throw new RuntimeException("Ya existe una instalación con el código: " + dto.getCodigoInstalacion());
        }

        // Crear instalación
        Instalacion instalacion = Instalacion.builder()
                .codigoInstalacion(dto.getCodigoInstalacion())
                .idUsuario(dto.getIdUsuario())
                .idCalle(dto.getIdCalle())
                .idCategoria(dto.getIdCategoria())
                .idZona(dto.getIdZona())
                .idRuta(dto.getIdRuta())
                .idManzana(dto.getIdManzana())
                .idSecuencia(dto.getIdSecuencia())
                .celular(dto.getCelular())
                .estado("A") // Activo por defecto
                .build();

        instalacion = instalacionRepository.save(instalacion);

        log.info("Instalación creada exitosamente. ID: {}, Código: {}",
                instalacion.getIdInstalacion(), instalacion.getCodigoInstalacion());

        return mapearInstalacionADTO(instalacion, usuario.getNombreCompleto());
    }

    /**
     * Lista todos los usuarios activos
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        log.info("Listando usuarios activos");
        List<Cliente> usuarios = usuarioRepository.findAllActivos();

        return usuarios.stream()
                .map(cliente -> {
                    Integer totalInstalaciones = usuarioRepository.contarInstalacionesActivas(cliente.getIdUsuario());
                    return mapearClienteADTO(cliente, totalInstalaciones);
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca usuarios por término de búsqueda
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarUsuarios(String busqueda) {
        log.info("Buscando usuarios con término: {}", busqueda);
        List<Cliente> usuarios = usuarioRepository.buscarUsuarios(busqueda);

        return usuarios.stream()
                .map(cliente -> {
                    Integer totalInstalaciones = usuarioRepository.contarInstalacionesActivas(cliente.getIdUsuario());
                    return mapearClienteADTO(cliente, totalInstalaciones);
                })
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario con todas sus instalaciones
     */
    @Transactional(readOnly = true)
    public UsuarioConInstalacionesDTO obtenerUsuarioConInstalaciones(Integer idUsuario) {
        log.info("Obteniendo usuario {} con sus instalaciones", idUsuario);

        Cliente usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Instalacion> instalaciones = instalacionRepository.findByIdUsuario(idUsuario);

        List<InstalacionDTO> instalacionesDTO = instalaciones.stream()
                .map(inst -> mapearInstalacionADTO(inst, usuario.getNombreCompleto()))
                .collect(Collectors.toList());

        // Calcular estadísticas
        int totalInstalaciones = instalaciones.size();
        int activas = (int) instalaciones.stream().filter(i -> "A".equals(i.getEstado())).count();
        int cortadas = (int) instalaciones.stream().filter(i -> "C".equals(i.getEstado())).count();
        int inactivas = (int) instalaciones.stream().filter(i -> "I".equals(i.getEstado())).count();

        UsuarioDTO usuarioDTO = mapearClienteADTO(usuario, totalInstalaciones);

        return UsuarioConInstalacionesDTO.builder()
                .usuario(usuarioDTO)
                .instalaciones(instalacionesDTO)
                .totalInstalaciones(totalInstalaciones)
                .instalacionesActivas(activas)
                .instalacionesCortadas(cortadas)
                .instalacionesInactivas(inactivas)
                .build();
    }

    /**
     * Lista instalaciones de un usuario
     */
    @Transactional(readOnly = true)
    public List<InstalacionDTO> listarInstalacionesPorUsuario(Integer idUsuario) {
        log.info("Listando instalaciones del usuario: {}", idUsuario);

        Cliente usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Instalacion> instalaciones = instalacionRepository.findByIdUsuario(idUsuario);

        return instalaciones.stream()
                .map(inst -> mapearInstalacionADTO(inst, usuario.getNombreCompleto()))
                .collect(Collectors.toList());
    }

    /**
     * Mapea Cliente a DTO
     */
    private UsuarioDTO mapearClienteADTO(Cliente cliente, Integer totalInstalaciones) {
        return UsuarioDTO.builder()
                .idUsuario(cliente.getIdUsuario())
                .paterno(cliente.getPaterno())
                .materno(cliente.getMaterno())
                .nombres(cliente.getNombres())
                .nombreCompleto(cliente.getNombreCompleto())
                .sexo(String.valueOf(cliente.getSexo()))
                .ci(cliente.getCi())
                .nit(cliente.getNit())
                .fecNacimiento(cliente.getFecNacimiento())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .celular(cliente.getCelular())
                .idEstadoCivil(cliente.getEstadoCivil() != null ? cliente.getEstadoCivil().getIdEstadoCivil() : null)
                .estadoCivil(cliente.getEstadoCivil() != null ? cliente.getEstadoCivil().getDescripcion() : null)
                .idTipoUsuario(cliente.getTipoUsuario() != null ? cliente.getTipoUsuario().getIdTipoUsuario() : null)
                .tipoUsuario(cliente.getTipoUsuario() != null ? cliente.getTipoUsuario().getDescripcion() : null)
                .totalInstalaciones(totalInstalaciones)
                .build();
    }

    /**
     * Mapea Instalacion a DTO
     */
    private InstalacionDTO mapearInstalacionADTO(Instalacion instalacion, String nombreCliente) {
        String estadoDesc = switch (instalacion.getEstado()) {
            case "A" -> "Activa";
            case "C" -> "Cortada";
            case "I" -> "Inactiva";
            default -> "Desconocido";
        };

        return InstalacionDTO.builder()
                .idInstalacion(instalacion.getIdInstalacion())
                .codigoInstalacion(instalacion.getCodigoInstalacion())
                .idUsuario(instalacion.getIdUsuario())
                .nombreCliente(nombreCliente)
                .celular(instalacion.getCelular())
                .estado(instalacion.getEstado())
                .estadoDescripcion(estadoDesc)
                .idCategoria(instalacion.getIdCategoria())
                .build();
    }
}
