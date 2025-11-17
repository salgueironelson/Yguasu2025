package bo.gob.yguasu.modules.usuarios.repository;

import bo.gob.yguasu.modules.instalaciones.domain.Instalacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para instalaciones relacionadas con usuarios
 */
@Repository
public interface InstalacionUsuarioRepository extends JpaRepository<Instalacion, Integer> {

    /**
     * Lista todas las instalaciones de un usuario
     */
    @Query(value = """
        SELECT i.*
        FROM cnt_instalaciones i
        WHERE i.id_usuario = :idUsuario
        AND i._estado != 'X'
        ORDER BY i.codigo_instalacion
        """, nativeQuery = true)
    List<Instalacion> findByIdUsuario(@Param("idUsuario") Integer idUsuario);

    /**
     * Cuenta instalaciones por estado de un usuario
     */
    @Query(value = """
        SELECT COUNT(*)
        FROM cnt_instalaciones
        WHERE id_usuario = :idUsuario
        AND _estado = :estado
        """, nativeQuery = true)
    Integer countByUsuarioAndEstado(@Param("idUsuario") Integer idUsuario, @Param("estado") String estado);

    /**
     * Verifica si existe código de instalación
     */
    boolean existsByCodigoInstalacion(Integer codigoInstalacion);
}
