package bo.gob.yguasu.modules.reclamos.service;

import bo.gob.yguasu.common.exception.BadRequestException;
import bo.gob.yguasu.common.exception.ResourceNotFoundException;
import bo.gob.yguasu.modules.clientes.repository.ClienteRepository;
import bo.gob.yguasu.modules.reclamos.domain.Paso;
import bo.gob.yguasu.modules.reclamos.domain.Reclamo;
import bo.gob.yguasu.modules.reclamos.dto.*;
import bo.gob.yguasu.modules.reclamos.repository.PasoRepository;
import bo.gob.yguasu.modules.reclamos.repository.ReclamoRepository;
import bo.gob.yguasu.modules.reclamos.repository.TUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final PasoRepository pasoRepository;
    private final TUsuarioRepository tUsuarioRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public ReclamoDTO crearReclamo(ReclamoCreateDTO dto, Integer idUsuario) {
        log.info("Creando nuevo reclamo para código instalación: {}", dto.getCodigoInstalacion());

        // Generar número de reclamo
        Integer maxNumero = reclamoRepository.findMaxNumero().orElse(0);
        Integer nuevoNumero = maxNumero + 1;

        // Validar si tiene instalación
        String nombreCliente = null;
        if (dto.getCodigoInstalacion() != null) {
            // TODO: Obtener datos de la instalación
            if (dto.getReclamante() == null) {
                throw new BadRequestException("Debe proporcionar el nombre del reclamante");
            }
        } else {
            // Reclamo sin instalación - validar nombre reclamante
            if (dto.getReclamante() == null || dto.getReclamante().isEmpty()) {
                throw new BadRequestException("El nombre del reclamante es requerido para reclamos sin instalación");
            }
        }

        // Crear reclamo
        Reclamo reclamo = Reclamo.builder()
                .numero(nuevoNumero)
                .codigoInstalacion(dto.getCodigoInstalacion())
                .celular(dto.getCelular())
                .tipoReclamo(dto.getTipoReclamo())
                .reclamante(dto.getReclamante())
                .detalle(dto.getDetalle())
                .fechaReclamo(dto.getFechaReclamo())
                .estado("PENDIENTE")
                .ubicacion(dto.getUbicacion())
                .departamento(dto.getDepartamento())
                .foto(dto.getFoto())
                .usuarioRegistro(idUsuario)
                .usuarioActual(idUsuario)
                .fechaRegistro(LocalDateTime.now())
                .build();

        reclamo = reclamoRepository.save(reclamo);

        // Crear primer paso (CREACION)
        Paso paso = Paso.builder()
                .idReclamo(String.valueOf(reclamo.getId()))
                .codigoInstalacion(dto.getCodigoInstalacion())
                .paso(1)
                .detalle("Reclamo creado")
                .tipoPaso("CREACION")
                .usuario(idUsuario)
                .estado("ACTIVO")
                .comentario("Registro inicial del reclamo")
                .fechaRegistro(LocalDateTime.now())
                .fechaInicio(LocalDateTime.now())
                .build();

        pasoRepository.save(paso);

        log.info("Reclamo creado exitosamente con ID: {} y número: {}", reclamo.getId(), reclamo.getNumero());

        return convertToDTO(reclamo);
    }

    @Transactional(readOnly = true)
    public Page<ReclamoDTO> listarConFiltros(ReclamoFilterDTO filtros, Pageable pageable) {
        log.info("Listando reclamos con filtros: {}", filtros);

        Page<Reclamo> reclamos = reclamoRepository.findByFilters(
                filtros.getBusqueda(),
                filtros.getTipoReclamo(),
                filtros.getEstado(),
                filtros.getDepartamento(),
                filtros.getProcedente(),
                filtros.getUsuarioActual(),
                filtros.getFechaDesde(),
                filtros.getFechaHasta(),
                pageable
        );

        return reclamos.map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public ReclamoDTO obtenerPorId(Integer id) {
        Reclamo reclamo = reclamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamo", "id", id));

        return convertToDTO(reclamo);
    }

    @Transactional(readOnly = true)
    public List<PasoDTO> obtenerHistorial(Integer idReclamo) {
        List<Paso> pasos = pasoRepository.findByIdReclamoOrderByPaso(String.valueOf(idReclamo));

        return pasos.stream()
                .map(this::convertPasoToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReclamoDTO agregarComentario(Integer idReclamo, ComentarioDTO dto, Integer idUsuario) {
        log.info("Agregando comentario al reclamo ID: {}", idReclamo);

        Reclamo reclamo = reclamoRepository.findById(idReclamo)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamo", "id", idReclamo));

        // Validar que el usuario actual pueda comentar
        if (!reclamo.getUsuarioActual().equals(idUsuario)) {
            throw new BadRequestException("Solo el usuario asignado puede agregar comentarios");
        }

        // Obtener siguiente número de paso
        Integer maxPaso = pasoRepository.findMaxPasoByIdReclamo(String.valueOf(idReclamo));
        Integer nuevoPaso = (maxPaso != null ? maxPaso : 0) + 1;

        // Crear paso de comentario
        Paso paso = Paso.builder()
                .idReclamo(String.valueOf(idReclamo))
                .codigoInstalacion(reclamo.getCodigoInstalacion())
                .paso(nuevoPaso)
                .detalle("Comentario agregado")
                .tipoPaso("COMENTARIO")
                .usuario(idUsuario)
                .estado("ACTIVO")
                .comentario(dto.getComentario())
                .fechaRegistro(LocalDateTime.now())
                .build();

        pasoRepository.save(paso);

        // Actualizar estado del reclamo si estaba pendiente
        if ("PENDIENTE".equals(reclamo.getEstado())) {
            reclamo.setEstado("EN_PROCESO");
            reclamoRepository.save(reclamo);
        }

        log.info("Comentario agregado exitosamente al reclamo ID: {}", idReclamo);

        return convertToDTO(reclamo);
    }

    @Transactional
    public ReclamoDTO transferirReclamo(Integer idReclamo, TransferenciaDTO dto, Integer idUsuario) {
        log.info("Transfiriendo reclamo ID: {} al usuario: {}", idReclamo, dto.getUsuarioDestino());

        Reclamo reclamo = reclamoRepository.findById(idReclamo)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamo", "id", idReclamo));

        // Validar usuario destino
        tUsuarioRepository.findById(dto.getUsuarioDestino())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", dto.getUsuarioDestino()));

        // Obtener siguiente número de paso
        Integer maxPaso = pasoRepository.findMaxPasoByIdReclamo(String.valueOf(idReclamo));
        Integer nuevoPaso = (maxPaso != null ? maxPaso : 0) + 1;

        // Crear paso de transferencia
        Paso paso = Paso.builder()
                .idReclamo(String.valueOf(idReclamo))
                .codigoInstalacion(reclamo.getCodigoInstalacion())
                .paso(nuevoPaso)
                .detalle("Reclamo transferido")
                .tipoPaso("TRANSFERENCIA")
                .usuario(idUsuario)
                .usuarioDestino(dto.getUsuarioDestino())
                .estado("TRANSFERIDO")
                .comentario(dto.getComentario())
                .fechaRegistro(LocalDateTime.now())
                .fechaInicio(LocalDateTime.now())
                .build();

        pasoRepository.save(paso);

        // Actualizar reclamo
        reclamo.setUsuarioActual(dto.getUsuarioDestino());
        reclamo.setEstado("TRANSFERIDO");
        reclamoRepository.save(reclamo);

        log.info("Reclamo transferido exitosamente");

        return convertToDTO(reclamo);
    }

    @Transactional
    public ReclamoDTO concluirReclamo(Integer idReclamo, ConclusionDTO dto, Integer idUsuario) {
        log.info("Concluyendo reclamo ID: {}", idReclamo);

        Reclamo reclamo = reclamoRepository.findById(idReclamo)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamo", "id", idReclamo));

        // Validar que el usuario actual pueda concluir
        if (!reclamo.getUsuarioActual().equals(idUsuario)) {
            throw new BadRequestException("Solo el usuario asignado puede concluir el reclamo");
        }

        // Obtener siguiente número de paso
        Integer maxPaso = pasoRepository.findMaxPasoByIdReclamo(String.valueOf(idReclamo));
        Integer nuevoPaso = (maxPaso != null ? maxPaso : 0) + 1;

        // Crear paso de conclusión
        Paso paso = Paso.builder()
                .idReclamo(String.valueOf(idReclamo))
                .codigoInstalacion(reclamo.getCodigoInstalacion())
                .paso(nuevoPaso)
                .detalle("Reclamo concluido")
                .tipoPaso("CONCLUSION")
                .usuario(idUsuario)
                .estado("CONCLUIDO")
                .comentario(dto.getConclusion())
                .fechaRegistro(LocalDateTime.now())
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now())
                .build();

        pasoRepository.save(paso);

        // Actualizar reclamo
        reclamo.setEstado("CONCLUIDO");
        reclamo.setProcedente(dto.getProcedente() ? "SI" : "NO");
        reclamo.setConclusion(dto.getConclusion());
        reclamo.setFechaSolucion(LocalDateTime.now());
        reclamoRepository.save(reclamo);

        log.info("Reclamo concluido exitosamente");

        return convertToDTO(reclamo);
    }

    // Métodos auxiliares de conversión
    private ReclamoDTO convertToDTO(Reclamo reclamo) {
        ReclamoDTO dto = ReclamoDTO.builder()
                .id(reclamo.getId())
                .numero(reclamo.getNumero())
                .codigoInstalacion(reclamo.getCodigoInstalacion())
                .celular(reclamo.getCelular())
                .tipoReclamo(reclamo.getTipoReclamo())
                .departamento(reclamo.getDepartamento())
                .reclamante(reclamo.getReclamante())
                .detalle(reclamo.getDetalle())
                .fechaReclamo(reclamo.getFechaReclamo())
                .fechaSolucion(reclamo.getFechaSolucion())
                .foto(reclamo.getFoto())
                .estado(reclamo.getEstado())
                .conclusion(reclamo.getConclusion())
                .ubicacion(reclamo.getUbicacion())
                .procedente(reclamo.getProcedente())
                .usuarioActual(reclamo.getUsuarioActual())
                .usuarioRegistro(reclamo.getUsuarioRegistro())
                .fechaRegistro(reclamo.getFechaRegistro())
                .build();

        // TODO: Obtener datos del cliente si tiene instalación
        // TODO: Obtener nombres de usuarios

        return dto;
    }

    private PasoDTO convertPasoToDTO(Paso paso) {
        PasoDTO dto = PasoDTO.builder()
                .id(paso.getId())
                .paso(paso.getPaso())
                .detalle(paso.getDetalle())
                .tipoPaso(paso.getTipoPaso())
                .comentario(paso.getComentario())
                .usuario(paso.getUsuario())
                .usuarioDestino(paso.getUsuarioDestino())
                .fechaRegistro(paso.getFechaRegistro())
                .fechaInicio(paso.getFechaInicio())
                .fechaFin(paso.getFechaFin())
                .estado(paso.getEstado())
                .build();

        // TODO: Obtener nombres de usuarios

        return dto;
    }
}
