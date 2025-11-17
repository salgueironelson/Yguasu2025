package bo.gob.yguasu.modules.usuarios.repository;

import bo.gob.yguasu.modules.clientes.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio extendido para gestión de usuarios/clientes
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Cliente, Integer> {

    /**
     * Busca usuario por CI
     */
    Optional<Cliente> findByCi(String ci);

    /**
     * Busca usuarios activos
     */
    @Query("SELECT c FROM Cliente c WHERE c.estado = 'A' ORDER BY c.nombreCompleto")
    List<Cliente> findAllActivos();

    /**
     * Busca usuarios por búsqueda general
     */
    @Query("""
        SELECT c FROM Cliente c
        WHERE c.estado = 'A'
        AND (
            LOWER(c.nombreCompleto) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            OR LOWER(c.ci) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            OR LOWER(c.nit) LIKE LOWER(CONCAT('%', :busqueda, '%'))
        )
        ORDER BY c.nombreCompleto
        """)
    List<Cliente> buscarUsuarios(@Param("busqueda") String busqueda);

    /**
     * Verifica si existe CI
     */
    boolean existsByCi(String ci);

    /**
     * Cuenta instalaciones por usuario
     */
    @Query(value = """
        SELECT COUNT(*)
        FROM cnt_instalaciones
        WHERE id_usuario = :idUsuario
        AND _estado = 'A'
        """, nativeQuery = true)
    Integer contarInstalacionesActivas(@Param("idUsuario") Integer idUsuario);
}
