package bo.gob.yguasu.modules.reclamos.repository;

import bo.gob.yguasu.modules.reclamos.domain.Paso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasoRepository extends JpaRepository<Paso, Integer> {

    @Query("SELECT p FROM Paso p WHERE p.idReclamo = :idReclamo ORDER BY p.paso ASC")
    List<Paso> findByIdReclamoOrderByPaso(@Param("idReclamo") String idReclamo);

    @Query("SELECT MAX(p.paso) FROM Paso p WHERE p.idReclamo = :idReclamo")
    Integer findMaxPasoByIdReclamo(@Param("idReclamo") String idReclamo);
}
