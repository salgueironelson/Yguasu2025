package bo.gob.yguasu.modules.servicios.repository;

import bo.gob.yguasu.modules.servicios.domain.InstalacionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para servicios asignados a instalaciones
 */
@Repository
public interface InstalacionServicioRepository extends JpaRepository<InstalacionServicio, Integer> {

    /**
     * Lista servicios activos de una instalación con información completa
     */
    @Query(value = """
        SELECT
            cis.id_instalacion_servicio,
            cis.id_instalacion,
            cis.id_servicio,
            s.servicio AS nombre_servicio,
            cis.id_medidor,
            m.serie AS serie_medidor,
            cis._estado
        FROM com_ins_servicios cis
        INNER JOIN com_servicios s ON cis.id_servicio = s.id_servicio
        LEFT JOIN tcn_medidores m ON cis.id_medidor = m.id_medidor
        WHERE cis.id_instalacion = :idInstalacion
          AND cis._activo = TRUE
        ORDER BY s.servicio
        """, nativeQuery = true)
    List<Object[]> findServiciosByInstalacion(@Param("idInstalacion") Integer idInstalacion);

    /**
     * Verifica si una instalación ya tiene un servicio asignado (activo)
     */
    @Query(value = """
        SELECT COUNT(*) > 0
        FROM com_ins_servicios
        WHERE id_instalacion = :idInstalacion
          AND id_servicio = :idServicio
          AND _activo = TRUE
        """, nativeQuery = true)
    boolean existeServicioActivo(@Param("idInstalacion") Integer idInstalacion,
                                  @Param("idServicio") Integer idServicio);

    /**
     * Busca un servicio activo de una instalación
     */
    Optional<InstalacionServicio> findByIdInstalacionAndIdServicioAndActivoTrue(
            Integer idInstalacion, Integer idServicio);
}
