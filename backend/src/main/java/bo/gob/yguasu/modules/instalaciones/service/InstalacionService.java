package bo.gob.yguasu.modules.instalaciones.service;

import bo.gob.yguasu.modules.instalaciones.dto.InstalacionDataDTO;
import bo.gob.yguasu.modules.instalaciones.repository.InstalacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio para gestión de instalaciones
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InstalacionService {

    private final InstalacionRepository instalacionRepository;

    /**
     * Busca los datos completos de una instalación por su código
     *
     * @param codigoInstalacion Código de instalación a buscar
     * @return DTO con datos completos o Optional.empty() si no existe
     */
    public Optional<InstalacionDataDTO> buscarPorCodigo(Integer codigoInstalacion) {
        log.info("Buscando instalación con código: {}", codigoInstalacion);

        Optional<Object[]> resultado = instalacionRepository.findDataByCodigoInstalacion(codigoInstalacion);

        if (resultado.isEmpty()) {
            log.warn("No se encontró instalación con código: {}", codigoInstalacion);
            return Optional.empty();
        }

        Object[] data = resultado.get();

        InstalacionDataDTO dto = InstalacionDataDTO.builder()
                .idInstalacion(data[0] != null ? ((Number) data[0]).intValue() : null)
                .codigoInstalacion(data[1] != null ? ((Number) data[1]).intValue() : null)
                .nombreCompleto((String) data[2])
                .calle((String) data[3])
                .categoria((String) data[4])
                .celular((String) data[5])
                .catastro((String) data[6])
                .medidor((String) data[7])
                .build();

        log.info("Instalación encontrada: {}", dto.getCodigoInstalacion());
        return Optional.of(dto);
    }
}
