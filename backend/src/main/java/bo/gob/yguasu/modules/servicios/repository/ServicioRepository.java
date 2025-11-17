package bo.gob.yguasu.modules.servicios.repository;

import bo.gob.yguasu.modules.servicios.domain.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para servicios disponibles
 */
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    /**
     * Lista servicios activos de tipo servicio = 1 (servicios de instalación)
     */
    @Query(value = """
        SELECT s.id_servicio, s.servicio, s.id_tipo_servicio
        FROM com_servicios s
        WHERE s.id_tipo_servicio = 1
          AND s._activo = TRUE
        ORDER BY s.servicio
        """, nativeQuery = true)
    List<Object[]> findServiciosDisponibles();
}
