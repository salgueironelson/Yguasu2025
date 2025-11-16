package bo.gob.yguasu.modules.cortes_reconexiones.service;

import bo.gob.yguasu.modules.cortes_reconexiones.domain.Corte;
import bo.gob.yguasu.modules.cortes_reconexiones.domain.Reconexion;
import bo.gob.yguasu.modules.cortes_reconexiones.dto.*;
import bo.gob.yguasu.modules.cortes_reconexiones.repository.CorteRepository;
import bo.gob.yguasu.modules.cortes_reconexiones.repository.ReconexionRepository;
import bo.gob.yguasu.modules.instalaciones.domain.Instalacion;
import bo.gob.yguasu.modules.instalaciones.repository.InstalacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
}
