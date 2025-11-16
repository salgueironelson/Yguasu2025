package bo.gob.yguasu.modules.cortes_reconexiones.repository;

import bo.gob.yguasu.modules.cortes_reconexiones.domain.Plomero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para plomeros
 */
@Repository
public interface PlomeroRepository extends JpaRepository<Plomero, Integer> {

    /**
     * Busca todos los plomeros activos
     */
    @Query("SELECT p FROM Plomero p WHERE p.estado = 'A' ORDER BY p.nombreCompleto")
    List<Plomero> findAllActivos();

    /**
     * Busca plomero por DNI
     */
    Plomero findByDni(String dni);
}
