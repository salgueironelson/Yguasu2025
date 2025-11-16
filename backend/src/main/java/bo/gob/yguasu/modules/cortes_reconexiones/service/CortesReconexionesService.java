package bo.gob.yguasu.modules.cortes_reconexiones.service;

import bo.gob.yguasu.modules.cortes_reconexiones.domain.Corte;
import bo.gob.yguasu.modules.cortes_reconexiones.domain.Plomero;
import bo.gob.yguasu.modules.cortes_reconexiones.domain.Reconexion;
import bo.gob.yguasu.modules.cortes_reconexiones.dto.*;
import bo.gob.yguasu.modules.cortes_reconexiones.repository.CorteRepository;
import bo.gob.yguasu.modules.cortes_reconexiones.repository.PlomeroRepository;
import bo.gob.yguasu.modules.cortes_reconexiones.repository.ReconexionRepository;
import bo.gob.yguasu.modules.instalaciones.domain.Instalacion;
import bo.gob.yguasu.modules.instalaciones.repository.InstalacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de cortes y reconexiones de servicio
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CortesReconexionesService {

    private final CorteRepository corteRepository;
    private final ReconexionRepository reconexionRepository;
    private final InstalacionRepository instalacionRepository;
    private final PlomeroRepository plomeroRepository;
    private final JdbcTemplate jdbcTemplate;

    /**
     * Realiza un corte de servicio
     * Crea registro en com_cortes y actualiza estado de instalación a 'C' (Cortado)
     */
    @Transactional
    public CorteDTO cortarServicio(CorteCreateDTO dto, Integer idUsuario) {
        log.info("Cortando servicio de instalación: {}", dto.getCodigoInstalacion());

        // Buscar instalación
        Instalacion instalacion = instalacionRepository.findByCodigoInstalacion(dto.getCodigoInstalacion())
                .orElseThrow(() -> new RuntimeException("Instalación no encontrada: " + dto.getCodigoInstalacion()));

        // Validar que no esté ya cortada
        if ("C".equals(instalacion.getEstado())) {
            throw new RuntimeException("La instalación ya está en corte");
        }

        // Validar que esté activa (no puede cortar si está inactiva)
        if ("I".equals(instalacion.getEstado())) {
            throw new RuntimeException("No se puede cortar una instalación inactiva");
        }

        // Crear registro de corte
        Corte corte = Corte.builder()
                .idInstalacion(instalacion.getIdInstalacion())
                .motivo(dto.getMotivo())
                .observaciones(dto.getObservaciones())
                .idPlomero(dto.getIdPlomero())
                .idUsuario(idUsuario)
                .fechaCorte(LocalDateTime.now())
                .estado("A")
                .build();

        corte = corteRepository.save(corte);

        // Actualizar estado de instalación a Cortado
        instalacion.setEstado("C");
        instalacionRepository.save(instalacion);

        log.info("Corte registrado exitosamente. ID Corte: {}", corte.getIdCorte());

        // Retornar DTO
        return CorteDTO.builder()
                .idCorte(corte.getIdCorte())
                .idInstalacion(instalacion.getIdInstalacion())
                .codigoInstalacion(instalacion.getCodigoInstalacion())
                .fechaCorte(corte.getFechaCorte())
                .motivo(corte.getMotivo())
                .observaciones(corte.getObservaciones())
                .estado(corte.getEstado())
                .build();
    }

    /**
     * Realiza una reconexión de servicio
     * Crea registro en com_reconexiones y actualiza estado de instalación a 'A' (Activo)
     */
    @Transactional
    public ReconexionDTO reconectarServicio(ReconexionCreateDTO dto, Integer idUsuario) {
        log.info("Reconectando servicio de instalación: {}", dto.getCodigoInstalacion());

        // Buscar instalación
        Instalacion instalacion = instalacionRepository.findByCodigoInstalacion(dto.getCodigoInstalacion())
                .orElseThrow(() -> new RuntimeException("Instalación no encontrada: " + dto.getCodigoInstalacion()));

        // Validar que esté cortada
        if (!"C".equals(instalacion.getEstado())) {
            throw new RuntimeException("La instalación no está en corte");
        }

        // Crear registro de reconexión
        Reconexion reconexion = Reconexion.builder()
                .idInstalacion(instalacion.getIdInstalacion())
                .motivo(dto.getMotivo())
                .observaciones(dto.getObservaciones())
                .idUsuario(idUsuario)
                .fechaReconexion(LocalDateTime.now())
                .estado("A")
                .build();

        reconexion = reconexionRepository.save(reconexion);

        // Actualizar estado de instalación a Activo
        instalacion.setEstado("A");
        instalacionRepository.save(instalacion);

        log.info("Reconexión registrada exitosamente. ID Reconexión: {}", reconexion.getIdReconexion());

        // Retornar DTO
        return ReconexionDTO.builder()
                .idReconexion(reconexion.getIdReconexion())
                .idInstalacion(instalacion.getIdInstalacion())
                .codigoInstalacion(instalacion.getCodigoInstalacion())
                .fechaReconexion(reconexion.getFechaReconexion())
                .motivo(reconexion.getMotivo())
                .observaciones(reconexion.getObservaciones())
                .estado(reconexion.getEstado())
                .build();
    }

    /**
     * Obtiene el historial completo de cortes y reconexiones de una instalación
     */
    @Transactional(readOnly = true)
    public HistorialCortesReconexionesDTO obtenerHistorial(Integer codigoInstalacion) {
        log.info("Obteniendo historial de cortes/reconexiones de instalación: {}", codigoInstalacion);

        // Buscar instalación
        Instalacion instalacion = instalacionRepository.findByCodigoInstalacion(codigoInstalacion)
                .orElseThrow(() -> new RuntimeException("Instalación no encontrada: " + codigoInstalacion));

        // Obtener cortes
        List<Object[]> cortesData = corteRepository.findCortesByCodigoInstalacion(codigoInstalacion);
        List<CorteDTO> cortes = mapearCortes(cortesData);

        // Obtener reconexiones
        List<Object[]> reconexionesData = reconexionRepository.findReconexionesByCodigoInstalacion(codigoInstalacion);
        List<ReconexionDTO> reconexiones = mapearReconexiones(reconexionesData);

        return HistorialCortesReconexionesDTO.builder()
                .codigoInstalacion(codigoInstalacion)
                .estadoActual(instalacion.getEstado())
                .cortes(cortes)
                .reconexiones(reconexiones)
                .build();
    }

    /**
     * Obtiene todos los cortes registrados
     */
    @Transactional(readOnly = true)
    public List<CorteDTO> listarCortes() {
        List<Object[]> data = corteRepository.findAllCortesConDetalles();
        return mapearCortes(data);
    }

    /**
     * Obtiene todas las reconexiones registradas
     */
    @Transactional(readOnly = true)
    public List<ReconexionDTO> listarReconexiones() {
        List<Object[]> data = reconexionRepository.findAllReconexionesConDetalles();
        return mapearReconexiones(data);
    }

    /**
     * Mapea resultados de query a DTOs de Corte
     */
    private List<CorteDTO> mapearCortes(List<Object[]> data) {
        return data.stream().map(row -> {
            Timestamp fechaCorteTimestamp = (Timestamp) row[4];
            LocalDateTime fechaCorte = fechaCorteTimestamp != null ? fechaCorteTimestamp.toLocalDateTime() : null;

            return CorteDTO.builder()
                    .idCorte(row[0] != null ? ((Number) row[0]).intValue() : null)
                    .idInstalacion(row[1] != null ? ((Number) row[1]).intValue() : null)
                    .codigoInstalacion(row[2] != null ? ((Number) row[2]).intValue() : null)
                    .nombreCliente((String) row[3])
                    .fechaCorte(fechaCorte)
                    .motivo((String) row[5])
                    .observaciones((String) row[6])
                    .usuarioRegistro((String) row[7])
                    .estado((String) row[8])
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Mapea resultados de query a DTOs de Reconexión
     */
    private List<ReconexionDTO> mapearReconexiones(List<Object[]> data) {
        return data.stream().map(row -> {
            Timestamp fechaReconexionTimestamp = (Timestamp) row[4];
            LocalDateTime fechaReconexion = fechaReconexionTimestamp != null ? fechaReconexionTimestamp.toLocalDateTime() : null;

            return ReconexionDTO.builder()
                    .idReconexion(row[0] != null ? ((Number) row[0]).intValue() : null)
                    .idInstalacion(row[1] != null ? ((Number) row[1]).intValue() : null)
                    .codigoInstalacion(row[2] != null ? ((Number) row[2]).intValue() : null)
                    .nombreCliente((String) row[3])
                    .fechaReconexion(fechaReconexion)
                    .motivo((String) row[5])
                    .observaciones((String) row[6])
                    .usuarioRegistro((String) row[7])
                    .estado((String) row[8])
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Lista todos los plomeros activos
     */
    @Transactional(readOnly = true)
    public List<PlomeroDTO> listarPlomerosActivos() {
        log.info("Listando plomeros activos");
        List<Plomero> plomeros = plomeroRepository.findAllActivos();

        return plomeros.stream().map(plomero -> PlomeroDTO.builder()
                .idPlomero(plomero.getIdPlomero())
                .nombreCompleto(plomero.getNombreCompleto())
                .dni(plomero.getDni())
                .direccion(plomero.getDireccion())
                .celular(plomero.getCelular())
                .estado(plomero.getEstado())
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * Busca instalaciones con deuda según filtros
     */
    @Transactional(readOnly = true)
    public List<InstalacionConDeudaDTO> buscarInstalacionesConDeuda(FiltrosCorteMasivoDTO filtros) {
        log.info("Buscando instalaciones con deuda. Zona inicial: {}, Zona final: {}, Cantidad facturas: {}",
                filtros.getZonaInicial(), filtros.getZonaFinal(), filtros.getCantidadFacturas());

        String sql = """
            SELECT DISTINCT
                i.id_instalacion,
                i.codigo_instalacion,
                (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombre_cliente,
                i.direccion,
                i.zona,
                COUNT(DISTINCT f.id_factura) AS cantidad_facturas,
                COALESCE(SUM(f.monto_total - COALESCE(f.monto_pagado, 0)), 0) AS monto_deuda,
                i._estado
            FROM cnt_instalaciones i
            JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
            LEFT JOIN fac_facturas f ON i.id_instalacion = f.id_instalacion
                AND f.estado = 'A'
                AND f.id_plan_pago IS NULL
                AND f.fec_pago IS NULL
                AND f._activo = TRUE
                AND f.im_tiposector = 13
            WHERE i._estado = 'A'
                AND i.zona BETWEEN ? AND ?
            GROUP BY i.id_instalacion, i.codigo_instalacion, u.nombres, u.paterno, u.materno, i.direccion, i.zona, i._estado
            HAVING COUNT(DISTINCT f.id_factura) >= ?
            ORDER BY i.zona, i.codigo_instalacion
            """;

        List<InstalacionConDeudaDTO> result = jdbcTemplate.query(sql,
                (rs, rowNum) -> InstalacionConDeudaDTO.builder()
                        .idInstalacion(rs.getInt("id_instalacion"))
                        .codigoInstalacion(rs.getInt("codigo_instalacion"))
                        .nombreCliente(rs.getString("nombre_cliente"))
                        .direccion(rs.getString("direccion"))
                        .zona(rs.getString("zona"))
                        .cantidadFacturasAdeudadas(rs.getInt("cantidad_facturas"))
                        .montoDeuda(rs.getBigDecimal("monto_deuda"))
                        .estado(rs.getString("_estado"))
                        .build(),
                filtros.getZonaInicial(),
                filtros.getZonaFinal(),
                filtros.getCantidadFacturas()
        );

        log.info("Se encontraron {} instalaciones con deuda", result.size());
        return result;
    }

    /**
     * Realiza corte masivo de servicios
     */
    @Transactional
    public CorteMasivoResponseDTO cortarServicioMasivo(CorteMasivoRequestDTO request, Integer idUsuario) {
        log.info("Iniciando corte masivo. Plomero ID: {}, Instalaciones: {}",
                request.getIdPlomero(), request.getIdsInstalaciones().size());

        // Validar que el plomero existe y está activo
        Plomero plomero = plomeroRepository.findById(request.getIdPlomero())
                .orElseThrow(() -> new RuntimeException("Plomero no encontrado"));

        if (!"A".equals(plomero.getEstado())) {
            throw new RuntimeException("El plomero no está activo");
        }

        List<CorteDTO> cortesRealizados = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        int exitosos = 0;
        int fallidos = 0;

        for (Integer idInstalacion : request.getIdsInstalaciones()) {
            try {
                // Buscar instalación por ID
                Instalacion instalacion = instalacionRepository.findById(idInstalacion)
                        .orElseThrow(() -> new RuntimeException("Instalación no encontrada: " + idInstalacion));

                // Validar estado
                if ("C".equals(instalacion.getEstado())) {
                    errores.add("Instalación " + instalacion.getCodigoInstalacion() + ": Ya está cortada");
                    fallidos++;
                    continue;
                }

                if ("I".equals(instalacion.getEstado())) {
                    errores.add("Instalación " + instalacion.getCodigoInstalacion() + ": Está inactiva");
                    fallidos++;
                    continue;
                }

                // Crear corte
                Corte corte = Corte.builder()
                        .idInstalacion(instalacion.getIdInstalacion())
                        .motivo(request.getMotivo())
                        .observaciones(request.getObservaciones())
                        .idPlomero(request.getIdPlomero())
                        .idUsuario(idUsuario)
                        .fechaCorte(LocalDateTime.now())
                        .estado("A")
                        .build();

                corte = corteRepository.save(corte);

                // Actualizar estado de instalación
                instalacion.setEstado("C");
                instalacionRepository.save(instalacion);

                // Agregar a la lista de exitosos
                cortesRealizados.add(CorteDTO.builder()
                        .idCorte(corte.getIdCorte())
                        .idInstalacion(instalacion.getIdInstalacion())
                        .codigoInstalacion(instalacion.getCodigoInstalacion())
                        .fechaCorte(corte.getFechaCorte())
                        .motivo(corte.getMotivo())
                        .observaciones(corte.getObservaciones())
                        .idPlomero(corte.getIdPlomero())
                        .nombrePlomero(plomero.getNombreCompleto())
                        .estado(corte.getEstado())
                        .build());

                exitosos++;
                log.info("Corte exitoso. Instalación: {}, ID Corte: {}",
                        instalacion.getCodigoInstalacion(), corte.getIdCorte());

            } catch (Exception e) {
                fallidos++;
                errores.add("Instalación ID " + idInstalacion + ": " + e.getMessage());
                log.error("Error al cortar instalación ID {}: {}", idInstalacion, e.getMessage());
            }
        }

        log.info("Corte masivo finalizado. Total: {}, Exitosos: {}, Fallidos: {}",
                request.getIdsInstalaciones().size(), exitosos, fallidos);

        return CorteMasivoResponseDTO.builder()
                .totalProcesados(request.getIdsInstalaciones().size())
                .totalExitosos(exitosos)
                .totalFallidos(fallidos)
                .cortesRealizados(cortesRealizados)
                .errores(errores)
                .build();
    }
}
