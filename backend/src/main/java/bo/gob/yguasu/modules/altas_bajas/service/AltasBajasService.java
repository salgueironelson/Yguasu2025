package bo.gob.yguasu.modules.altas_bajas.service;

import bo.gob.yguasu.modules.altas_bajas.domain.Alta;
import bo.gob.yguasu.modules.altas_bajas.domain.Baja;
import bo.gob.yguasu.modules.altas_bajas.dto.*;
import bo.gob.yguasu.modules.altas_bajas.repository.AltaRepository;
import bo.gob.yguasu.modules.altas_bajas.repository.BajaRepository;
import bo.gob.yguasu.modules.instalaciones.domain.Instalacion;
import bo.gob.yguasu.modules.instalaciones.repository.InstalacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de altas y bajas de instalaciones
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AltasBajasService {

    private final BajaRepository bajaRepository;
    private final AltaRepository altaRepository;
    private final InstalacionRepository instalacionRepository;

    /**
     * Da de baja una instalación
     * Crea registro en com_bajas y actualiza estado de instalación a 'I' (Inactivo)
     */
    @Transactional
    public BajaDTO darDeBaja(BajaCreateDTO dto, Integer idUsuario) {
        log.info("Dando de baja instalación: {}", dto.getCodigoInstalacion());

        // Buscar instalación
        Instalacion instalacion = instalacionRepository.findByCodigoInstalacion(dto.getCodigoInstalacion())
                .orElseThrow(() -> new RuntimeException("Instalación no encontrada: " + dto.getCodigoInstalacion()));

        // Validar que esté activa
        if ("I".equals(instalacion.getEstado())) {
            throw new RuntimeException("La instalación ya está dada de baja");
        }

        // Crear registro de baja
        Baja baja = Baja.builder()
                .idInstalacion(instalacion.getIdInstalacion())
                .motivo(dto.getMotivo())
                .observaciones(dto.getObservaciones())
                .idUsuario(idUsuario)
                .fechaBaja(LocalDateTime.now())
                .estado("A")
                .build();

        baja = bajaRepository.save(baja);

        // Actualizar estado de instalación a Inactivo
        instalacion.setEstado("I");
        instalacionRepository.save(instalacion);

        log.info("Baja registrada exitosamente. ID Baja: {}", baja.getIdBaja());

        // Retornar DTO
        return BajaDTO.builder()
                .idBaja(baja.getIdBaja())
                .idInstalacion(instalacion.getIdInstalacion())
                .codigoInstalacion(instalacion.getCodigoInstalacion())
                .fechaBaja(baja.getFechaBaja())
                .motivo(baja.getMotivo())
                .observaciones(baja.getObservaciones())
                .estado(baja.getEstado())
                .build();
    }

    /**
     * Da de alta una instalación
     * Crea registro en com_altas y actualiza estado de instalación a 'A' (Activo)
     */
    @Transactional
    public AltaDTO darDeAlta(AltaCreateDTO dto, Integer idUsuario) {
        log.info("Dando de alta instalación: {}", dto.getCodigoInstalacion());

        // Buscar instalación
        Instalacion instalacion = instalacionRepository.findByCodigoInstalacion(dto.getCodigoInstalacion())
                .orElseThrow(() -> new RuntimeException("Instalación no encontrada: " + dto.getCodigoInstalacion()));

        // Validar que esté inactiva
        if ("A".equals(instalacion.getEstado())) {
            throw new RuntimeException("La instalación ya está activa");
        }

        // Crear registro de alta
        Alta alta = Alta.builder()
                .idInstalacion(instalacion.getIdInstalacion())
                .motivo(dto.getMotivo())
                .observaciones(dto.getObservaciones())
                .idUsuario(idUsuario)
                .fechaAlta(LocalDateTime.now())
                .estado("A")
                .build();

        alta = altaRepository.save(alta);

        // Actualizar estado de instalación a Activo
        instalacion.setEstado("A");
        instalacionRepository.save(instalacion);

        log.info("Alta registrada exitosamente. ID Alta: {}", alta.getIdAlta());

        // Retornar DTO
        return AltaDTO.builder()
                .idAlta(alta.getIdAlta())
                .idInstalacion(instalacion.getIdInstalacion())
                .codigoInstalacion(instalacion.getCodigoInstalacion())
                .fechaAlta(alta.getFechaAlta())
                .motivo(alta.getMotivo())
                .observaciones(alta.getObservaciones())
                .estado(alta.getEstado())
                .build();
    }

    /**
     * Obtiene el historial completo de altas y bajas de una instalación
     */
    @Transactional(readOnly = true)
    public HistorialAltasBajasDTO obtenerHistorial(Integer codigoInstalacion) {
        log.info("Obteniendo historial de instalación: {}", codigoInstalacion);

        // Buscar instalación
        Instalacion instalacion = instalacionRepository.findByCodigoInstalacion(codigoInstalacion)
                .orElseThrow(() -> new RuntimeException("Instalación no encontrada: " + codigoInstalacion));

        // Obtener bajas
        List<Object[]> bajasData = bajaRepository.findBajasByCodigoInstalacion(codigoInstalacion);
        List<BajaDTO> bajas = mapearBajas(bajasData);

        // Obtener altas
        List<Object[]> altasData = altaRepository.findAltasByCodigoInstalacion(codigoInstalacion);
        List<AltaDTO> altas = mapearAltas(altasData);

        return HistorialAltasBajasDTO.builder()
                .codigoInstalacion(codigoInstalacion)
                .estadoActual(instalacion.getEstado())
                .bajas(bajas)
                .altas(altas)
                .build();
    }

    /**
     * Obtiene todas las bajas registradas
     */
    @Transactional(readOnly = true)
    public List<BajaDTO> listarBajas() {
        List<Object[]> data = bajaRepository.findAllBajasConDetalles();
        return mapearBajas(data);
    }

    /**
     * Obtiene todas las altas registradas
     */
    @Transactional(readOnly = true)
    public List<AltaDTO> listarAltas() {
        List<Object[]> data = altaRepository.findAllAltasConDetalles();
        return mapearAltas(data);
    }

    /**
     * Mapea resultados de query a DTOs de Baja
     */
    private List<BajaDTO> mapearBajas(List<Object[]> data) {
        return data.stream().map(row -> {
            Timestamp fechaBajaTimestamp = (Timestamp) row[4];
            LocalDateTime fechaBaja = fechaBajaTimestamp != null ? fechaBajaTimestamp.toLocalDateTime() : null;

            return BajaDTO.builder()
                    .idBaja(row[0] != null ? ((Number) row[0]).intValue() : null)
                    .idInstalacion(row[1] != null ? ((Number) row[1]).intValue() : null)
                    .codigoInstalacion(row[2] != null ? ((Number) row[2]).intValue() : null)
                    .nombreCliente((String) row[3])
                    .fechaBaja(fechaBaja)
                    .motivo((String) row[5])
                    .observaciones((String) row[6])
                    .usuarioRegistro((String) row[7])
                    .estado((String) row[8])
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * Mapea resultados de query a DTOs de Alta
     */
    private List<AltaDTO> mapearAltas(List<Object[]> data) {
        return data.stream().map(row -> {
            Timestamp fechaAltaTimestamp = (Timestamp) row[4];
            LocalDateTime fechaAlta = fechaAltaTimestamp != null ? fechaAltaTimestamp.toLocalDateTime() : null;

            return AltaDTO.builder()
                    .idAlta(row[0] != null ? ((Number) row[0]).intValue() : null)
                    .idInstalacion(row[1] != null ? ((Number) row[1]).intValue() : null)
                    .codigoInstalacion(row[2] != null ? ((Number) row[2]).intValue() : null)
                    .nombreCliente((String) row[3])
                    .fechaAlta(fechaAlta)
                    .motivo((String) row[5])
                    .observaciones((String) row[6])
                    .usuarioRegistro((String) row[7])
                    .estado((String) row[8])
                    .build();
        }).collect(Collectors.toList());
    }
}
