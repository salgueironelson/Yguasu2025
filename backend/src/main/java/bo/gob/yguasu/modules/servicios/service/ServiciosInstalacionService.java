package bo.gob.yguasu.modules.servicios.service;

import bo.gob.yguasu.modules.servicios.domain.InstalacionServicio;
import bo.gob.yguasu.modules.servicios.dto.*;
import bo.gob.yguasu.modules.servicios.repository.InstalacionServicioRepository;
import bo.gob.yguasu.modules.servicios.repository.MedidorRepository;
import bo.gob.yguasu.modules.servicios.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestión de servicios asignados a instalaciones
 */
@Service
@RequiredArgsConstructor
public class ServiciosInstalacionService {

    private final ServicioRepository servicioRepository;
    private final InstalacionServicioRepository instalacionServicioRepository;
    private final MedidorRepository medidorRepository;

    /**
     * Lista todos los servicios disponibles (tipo servicio = 1)
     */
    public List<ServicioDTO> listarServiciosDisponibles() {
        List<Object[]> results = servicioRepository.findServiciosDisponibles();
        List<ServicioDTO> servicios = new ArrayList<>();

        for (Object[] row : results) {
            ServicioDTO dto = ServicioDTO.builder()
                    .idServicio((Integer) row[0])
                    .servicio((String) row[1])
                    .idTipoServicio((Integer) row[2])
                    .build();
            servicios.add(dto);
        }

        return servicios;
    }

    /**
     * Lista servicios asignados a una instalación
     */
    public List<InstalacionServicioDTO> listarServiciosPorInstalacion(Integer idInstalacion) {
        List<Object[]> results = instalacionServicioRepository.findServiciosByInstalacion(idInstalacion);
        List<InstalacionServicioDTO> servicios = new ArrayList<>();

        for (Object[] row : results) {
            String estado = (String) row[6];
            String estadoDescripcion = switch (estado) {
                case "A" -> "Activo";
                case "I" -> "Inactivo";
                case "C" -> "Cortado";
                default -> "Desconocido";
            };

            InstalacionServicioDTO dto = InstalacionServicioDTO.builder()
                    .idInstalacionServicio((Integer) row[0])
                    .idInstalacion((Integer) row[1])
                    .idServicio((Integer) row[2])
                    .nombreServicio((String) row[3])
                    .idMedidor((Integer) row[4])
                    .serieMedidor((String) row[5])
                    .estado(estado)
                    .estadoDescripcion(estadoDescripcion)
                    .build();
            servicios.add(dto);
        }

        return servicios;
    }

    /**
     * Lista medidores disponibles (no asignados)
     */
    public List<MedidorDTO> listarMedidoresDisponibles() {
        List<Object[]> results = medidorRepository.findMedidoresDisponibles();
        List<MedidorDTO> medidores = new ArrayList<>();

        for (Object[] row : results) {
            MedidorDTO dto = MedidorDTO.builder()
                    .idMedidor((Integer) row[0])
                    .serie((String) row[1])
                    .build();
            medidores.add(dto);
        }

        return medidores;
    }

    /**
     * Asigna un servicio a una instalación
     */
    @Transactional
    public InstalacionServicioDTO asignarServicio(AsignarServicioDTO dto) {
        // Validar que no exista el servicio ya asignado
        if (instalacionServicioRepository.existeServicioActivo(dto.getIdInstalacion(), dto.getIdServicio())) {
            throw new RuntimeException("El servicio ya está asignado a esta instalación");
        }

        // Validar que el servicio exista y sea de tipo 1
        if (!servicioRepository.existsById(dto.getIdServicio())) {
            throw new RuntimeException("El servicio especificado no existe");
        }

        // Si se especifica medidor, validar que exista
        if (dto.getIdMedidor() != null && !medidorRepository.existsById(dto.getIdMedidor())) {
            throw new RuntimeException("El medidor especificado no existe");
        }

        // Crear la asignación
        InstalacionServicio instalacionServicio = InstalacionServicio.builder()
                .idInstalacion(dto.getIdInstalacion())
                .idServicio(dto.getIdServicio())
                .idMedidor(dto.getIdMedidor())
                .estado("A")
                .usuarioAlta("SYSTEM") // TODO: Obtener usuario del contexto de seguridad
                .fechaAlta(LocalDateTime.now())
                .activo(true)
                .build();

        InstalacionServicio saved = instalacionServicioRepository.save(instalacionServicio);

        // Obtener información completa para el retorno
        List<InstalacionServicioDTO> servicios = listarServiciosPorInstalacion(saved.getIdInstalacion());
        return servicios.stream()
                .filter(s -> s.getIdInstalacionServicio().equals(saved.getIdInstalacionServicio()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error al recuperar el servicio asignado"));
    }

    /**
     * Desactiva un servicio de una instalación
     */
    @Transactional
    public void desactivarServicio(Integer idInstalacionServicio) {
        InstalacionServicio instalacionServicio = instalacionServicioRepository
                .findById(idInstalacionServicio)
                .orElseThrow(() -> new RuntimeException("Servicio de instalación no encontrado"));

        instalacionServicio.setEstado("I");
        instalacionServicio.setActivo(false);
        instalacionServicio.setUsuarioModificacion("SYSTEM"); // TODO: Obtener usuario del contexto
        instalacionServicio.setFechaModificacion(LocalDateTime.now());

        instalacionServicioRepository.save(instalacionServicio);
    }
}
