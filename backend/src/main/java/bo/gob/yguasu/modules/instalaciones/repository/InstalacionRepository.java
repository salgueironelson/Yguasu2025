package bo.gob.yguasu.modules.instalaciones.repository;

import bo.gob.yguasu.modules.instalaciones.domain.Instalacion;
import bo.gob.yguasu.modules.instalaciones.dto.InstalacionDataDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestión de instalaciones
 */
@Repository
public interface InstalacionRepository extends JpaRepository<Instalacion, Integer> {

    /**
     * Busca una instalación por su código con todos los datos relacionados
     * Consulta compleja que obtiene: nombres, calle, categoría, catastro y medidor
     *
     * @param codigo Código de instalación a buscar
     * @return DTO con todos los datos de la instalación
     */
    @Query(value = """
        SELECT i.id_instalacion AS idInstalacion,
               i.codigo_instalacion AS codigoInstalacion,
               (u.nombres || ' ' || COALESCE(u.paterno, '') || ' ' || COALESCE(u.materno, '')) AS nombreCompleto,
               c.calle AS calle,
               ca.categoria AS categoria,
               i.celular AS celular,
               (z.zona || '-' || r.ruta || '-' || ma.manzana || '-' || s.secuencia) AS catastro,
               (SELECT m.serie
                FROM com_ins_servicios cis
                LEFT JOIN tcn_medidores m ON m.id_medidor = cis.id_medidor
                WHERE cis.id_instalacion = i.id_instalacion
                  AND cis.id_servicio = 1
                  AND cis._estado = 'A'
                LIMIT 1) AS medidor
        FROM cnt_instalaciones i
        JOIN cnt_usuarios u ON i.id_usuario = u.id_usuario
        JOIN tcn_calles c ON i.id_calle = c.id_calle
        JOIN com_categorias ca ON i.id_categoria = ca.id_categoria
        JOIN tcn_zonas z ON i.id_zona = z.id_zona
        JOIN tcn_rutas r ON i.id_ruta = r.id_ruta
        JOIN tcn_manzanas ma ON i.id_manzana = ma.id_manzana
        JOIN tcn_secuencias s ON i.id_secuencia = s.id_secuencia
        WHERE i.codigo_instalacion = :codigo
          AND i._estado = 'A'
        """, nativeQuery = true)
    Optional<Object[]> findDataByCodigoInstalacion(@Param("codigo") Integer codigo);

    /**
     * Busca una instalación por su código (método simple)
     *
     * @param codigoInstalacion Código a buscar
     * @return Instalación encontrada
     */
    Optional<Instalacion> findByCodigoInstalacion(Integer codigoInstalacion);
}
