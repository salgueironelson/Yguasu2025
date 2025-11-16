package bo.gob.yguasu.modules.reclamos.repository;

import bo.gob.yguasu.modules.reclamos.domain.TUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TUsuarioRepository extends JpaRepository<TUsuario, Integer> {

    Optional<TUsuario> findByUsuario(String usuario);

    @Query("SELECT u FROM TUsuario u WHERE u.estado = :estado ORDER BY u.nombre")
    List<TUsuario> findByEstado(@Param("estado") String estado);

    @Query("SELECT u FROM TUsuario u WHERE u.departamento = :departamento AND u.estado = 'ACTIVO' ORDER BY u.nombre")
    List<TUsuario> findByDepartamentoActivos(@Param("departamento") String departamento);
}
