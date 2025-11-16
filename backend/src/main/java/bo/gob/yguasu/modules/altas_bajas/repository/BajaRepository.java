package bo.gob.yguasu.modules.altas_bajas.repository;

import bo.gob.yguasu.modules.altas_bajas.domain.Baja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestión de bajas de instalaciones
 */
@Repository
public interface BajaRepository extends JpaRepository<Baja, Integer> {

    /**
     * Obtiene todas las bajas de una instalación
     */
    @Query("SELECT b FROM Baja b WHERE b.idInstalacion = :idInstalacion ORDER BY b.fechaBaja DESC")
    List<Baja> findByIdInstalacion(@Param("idInstalacion") Integer idInstalacion);

    /**
     * Obtiene bajas con información completa (join con instalaciones y usuarios)
     */
    @Query(value = """
        SELECT b.id_baja AS idBaja,
               b.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               b.fecha_baja AS fechaBaja,
               b.motivo AS motivo,
               b.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               b._estado AS estado
        FROM com_bajas b
        JOIN cnt_instalaciones i ON b.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON b.id_usuario = ur.id_usuario
        WHERE b._estado = 'A'
        ORDER BY b.fecha_baja DESC
        """, nativeQuery = true)
    List<Object[]> findAllBajasConDetalles();

    /**
     * Busca bajas por código de instalación
     */
    @Query(value = """
        SELECT b.id_baja AS idBaja,
               b.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               b.fecha_baja AS fechaBaja,
               b.motivo AS motivo,
               b.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               b._estado AS estado
        FROM com_bajas b
        JOIN cnt_instalaciones i ON b.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON b.id_usuario = ur.id_usuario
        WHERE i.codigo_instalacion = :codigo
          AND b._estado = 'A'
        ORDER BY b.fecha_baja DESC
        """, nativeQuery = true)
    List<Object[]> findBajasByCodigoInstalacion(@Param("codigo") Integer codigo);
}
