package bo.gob.yguasu.modules.servicios.repository;

import bo.gob.yguasu.modules.servicios.domain.Medidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para medidores
 */
@Repository
public interface MedidorRepository extends JpaRepository<Medidor, Integer> {

    /**
     * Lista medidores activos disponibles
     */
    @Query(value = """
        SELECT m.id_medidor, m.serie
        FROM tcn_medidores m
        WHERE m._activo = TRUE
          AND NOT EXISTS (
              SELECT 1 FROM com_ins_servicios cis
              WHERE cis.id_medidor = m.id_medidor
                AND cis._activo = TRUE
          )
        ORDER BY m.serie
        """, nativeQuery = true)
    List<Object[]> findMedidoresDisponibles();
}
