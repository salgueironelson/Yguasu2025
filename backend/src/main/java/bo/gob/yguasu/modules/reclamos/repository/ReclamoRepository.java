package bo.gob.yguasu.modules.reclamos.repository;

import bo.gob.yguasu.modules.reclamos.domain.Reclamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Integer> {

    @Query("SELECT MAX(r.numero) FROM Reclamo r")
    Optional<Integer> findMaxNumero();

    @Query("SELECT r FROM Reclamo r WHERE " +
            "(:busqueda IS NULL OR " +
            "   CAST(r.codigoInstalacion AS string) LIKE CONCAT('%', :busqueda, '%') OR " +
            "   LOWER(r.reclamante) LIKE LOWER(CONCAT('%', :busqueda, '%'))) AND " +
            "(:tipoReclamo IS NULL OR r.tipoReclamo = :tipoReclamo) AND " +
            "(:estado IS NULL OR r.estado = :estado) AND " +
            "(:departamento IS NULL OR r.departamento = :departamento) AND " +
            "(:procedente IS NULL OR r.procedente = :procedente) AND " +
            "(:usuarioActual IS NULL OR r.usuarioActual = :usuarioActual) AND " +
            "(:fechaDesde IS NULL OR r.fechaReclamo >= :fechaDesde) AND " +
            "(:fechaHasta IS NULL OR r.fechaReclamo <= :fechaHasta) " +
            "ORDER BY r.fechaRegistro DESC")
    Page<Reclamo> findByFilters(
            @Param("busqueda") String busqueda,
            @Param("tipoReclamo") String tipoReclamo,
            @Param("estado") String estado,
            @Param("departamento") String departamento,
            @Param("procedente") String procedente,
            @Param("usuarioActual") Integer usuarioActual,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            Pageable pageable
    );

    @Query("SELECT r FROM Reclamo r WHERE r.codigoInstalacion = :codigoInstalacion ORDER BY r.fechaRegistro DESC")
    Page<Reclamo> findByCodigoInstalacion(@Param("codigoInstalacion") Integer codigoInstalacion, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Reclamo r WHERE r.estado = :estado")
    Long countByEstado(@Param("estado") String estado);
}
