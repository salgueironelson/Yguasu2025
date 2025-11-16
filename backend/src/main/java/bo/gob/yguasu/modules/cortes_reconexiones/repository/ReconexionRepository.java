package bo.gob.yguasu.modules.cortes_reconexiones.repository;

import bo.gob.yguasu.modules.cortes_reconexiones.domain.Reconexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestión de reconexiones de servicio
 */
@Repository
public interface ReconexionRepository extends JpaRepository<Reconexion, Integer> {

    /**
     * Obtiene todas las reconexiones de una instalación
     */
    @Query("SELECT r FROM Reconexion r WHERE r.idInstalacion = :idInstalacion ORDER BY r.fechaReconexion DESC")
    List<Reconexion> findByIdInstalacion(@Param("idInstalacion") Integer idInstalacion);

    /**
     * Obtiene reconexiones con información completa (join con instalaciones y usuarios)
     */
    @Query(value = """
        SELECT r.id_reconexion AS idReconexion,
               r.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               r.fecha_reconexion AS fechaReconexion,
               r.motivo AS motivo,
               r.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               r._estado AS estado
        FROM com_reconexiones r
        JOIN cnt_instalaciones i ON r.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON r.id_usuario = ur.id_usuario
        WHERE r._estado = 'A'
        ORDER BY r.fecha_reconexion DESC
        """, nativeQuery = true)
    List<Object[]> findAllReconexionesConDetalles();

    /**
     * Busca reconexiones por código de instalación
     */
    @Query(value = """
        SELECT r.id_reconexion AS idReconexion,
               r.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               r.fecha_reconexion AS fechaReconexion,
               r.motivo AS motivo,
               r.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               r._estado AS estado
        FROM com_reconexiones r
        JOIN cnt_instalaciones i ON r.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON r.id_usuario = ur.id_usuario
        WHERE i.codigo_instalacion = :codigo
          AND r._estado = 'A'
        ORDER BY r.fecha_reconexion DESC
        """, nativeQuery = true)
    List<Object[]> findReconexionesByCodigoInstalacion(@Param("codigo") Integer codigo);
}
