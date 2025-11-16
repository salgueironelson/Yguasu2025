package bo.gob.yguasu.modules.clientes.repository;

import bo.gob.yguasu.modules.clientes.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByIdUsuarioAndEstado(Integer id, Character estado);

    @Query("SELECT c FROM Cliente c WHERE c.estado = :estado")
    Page<Cliente> findAllByEstado(@Param("estado") Character estado, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.estado = :estado AND " +
            "(LOWER(c.nombreCompleto) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.ci) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.nit) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Cliente> searchByEstado(
            @Param("estado") Character estado,
            @Param("search") String search,
            Pageable pageable
    );

    boolean existsByCi(String ci);

    boolean existsByNit(String nit);
}
