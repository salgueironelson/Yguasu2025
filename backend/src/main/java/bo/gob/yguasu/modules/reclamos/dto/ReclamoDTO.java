package bo.gob.yguasu.modules.reclamos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReclamoDTO {

    private Integer id;
    private Integer numero;
    private Integer codigoInstalacion;

    // Datos del cliente (si tiene instalación)
    private String nombreCliente;
    private String numeroMedidor;
    private String catastro;
    private String categoria;

    // Datos del reclamo
    private String celular;
    private String tipoReclamo;
    private String departamento;
    private String reclamante;
    private String detalle;
    private LocalDateTime fechaReclamo;
    private LocalDateTime fechaSolucion;
    private String foto;
    private String estado;
    private String conclusion;
    private String ubicacion;
    private String procedente;

    // Usuarios
    private Integer usuarioActual;
    private String nombreUsuarioActual;
    private Integer usuarioRegistro;
    private String nombreUsuarioRegistro;
    private LocalDateTime fechaRegistro;

    // Contadores
    private Integer totalPasos;
}
