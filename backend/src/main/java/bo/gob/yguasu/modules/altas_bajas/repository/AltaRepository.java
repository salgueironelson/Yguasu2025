package bo.gob.yguasu.modules.altas_bajas.repository;

import bo.gob.yguasu.modules.altas_bajas.domain.Alta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestión de altas de instalaciones
 */
@Repository
public interface AltaRepository extends JpaRepository<Alta, Integer> {

    /**
     * Obtiene todas las altas de una instalación
     */
    @Query("SELECT a FROM Alta a WHERE a.idInstalacion = :idInstalacion ORDER BY a.fechaAlta DESC")
    List<Alta> findByIdInstalacion(@Param("idInstalacion") Integer idInstalacion);

    /**
     * Obtiene altas con información completa (join con instalaciones y usuarios)
     */
    @Query(value = """
        SELECT a.id_alta AS idAlta,
               a.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               a.fecha_alta AS fechaAlta,
               a.motivo AS motivo,
               a.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               a._estado AS estado
        FROM com_altas a
        JOIN cnt_instalaciones i ON a.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON a.id_usuario = ur.id_usuario
        WHERE a._estado = 'A'
        ORDER BY a.fecha_alta DESC
        """, nativeQuery = true)
    List<Object[]> findAllAltasConDetalles();

    /**
     * Busca altas por código de instalación
     */
    @Query(value = """
        SELECT a.id_alta AS idAlta,
               a.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCliente,
               a.fecha_alta AS fechaAlta,
               a.motivo AS motivo,
               a.observaciones AS observaciones,
               (ur.nombres || ' ' || COALESCE(ur.paterno, '')) AS usuarioRegistro,
               a._estado AS estado
        FROM com_altas a
        JOIN cnt_instalaciones i ON a.id_instalacion = i.id_instalacion
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        LEFT JOIN _bp_usuarios ur ON a.id_usuario = ur.id_usuario
        WHERE i.codigo_instalacion = :codigo
          AND a._estado = 'A'
        ORDER BY a.fecha_alta DESC
        """, nativeQuery = true)
    List<Object[]> findAltasByCodigoInstalacion(@Param("codigo") Integer codigo);
}
