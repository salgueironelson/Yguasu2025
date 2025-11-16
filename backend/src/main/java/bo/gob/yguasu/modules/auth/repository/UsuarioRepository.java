package bo.gob.yguasu.modules.auth.repository;

import bo.gob.yguasu.modules.auth.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsuario(String usuario);

    Optional<Usuario> findByUsuarioAndEstado(String usuario, Character estado);

    boolean existsByUsuario(String usuario);
}
