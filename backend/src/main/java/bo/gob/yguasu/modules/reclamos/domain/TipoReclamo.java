package bo.gob.yguasu.modules.reclamos.domain;

public enum TipoReclamo {
    FALTA_AGUA("Falta de Agua"),
    BAJA_PRESION("Baja Presión"),
    FUGA_AGUA("Fuga de Agua"),
    MEDIDOR_DANADO("Medidor Dañado"),
    FACTURACION("Facturación"),
    ALCANTARILLADO("Alcantarillado"),
    RECONEXION("Reconexión"),
    CORTE_INDEBIDO("Corte Indebido"),
    ATENCION_CLIENTE("Atención al Cliente"),
    OTRO("Otro");

    private final String descripcion;

    TipoReclamo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
