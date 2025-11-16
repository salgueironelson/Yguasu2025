package bo.gob.yguasu.modules.cortes_reconexiones.repository;

import bo.gob.yguasu.modules.cortes_reconexiones.domain.Corte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestión de cortes de servicio
 */
@Repository
public interface CorteRepository extends JpaRepository<Corte, Integer> {

    /**
     * Obtiene todos los cortes de una instalación
     */
    @Query("SELECT c FROM Corte c WHERE c.idInstalacion = :idInstalacion ORDER BY c.fechaCorte DESC")
    List<Corte> findByIdInstalacion(@Param("idInstalacion") Integer idInstalacion);

    /**
     * Obtiene cortes con información completa (join con instalaciones y usuarios)
     */
    @Query(value = """
        SELECT c.id_corte AS idCorte,
               c.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               c.fecha_corte AS fechaCorte,
               c.motivo AS motivo,
               c.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               c._estado AS estado
        FROM com_cortes c
        JOIN cnt_instalaciones i ON c.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON c.id_usuario = ur.id_usuario
        WHERE c._estado = 'A'
        ORDER BY c.fecha_corte DESC
        """, nativeQuery = true)
    List<Object[]> findAllCortesConDetalles();

    /**
     * Busca cortes por código de instalación
     */
    @Query(value = """
        SELECT c.id_corte AS idCorte,
               c.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               c.fecha_corte AS fechaCorte,
               c.motivo AS motivo,
               c.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               c._estado AS estado
        FROM com_cortes c
        JOIN cnt_instalaciones i ON c.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON c.id_usuario = ur.id_usuario
        WHERE i.codigo_instalacion = :codigo
          AND c._estado = 'A'
        ORDER BY c.fecha_corte DESC
        """, nativeQuery = true)
    List<Object[]> findCortesByCodigoInstalacion(@Param("codigo") Integer codigo);
}
