package Modelo;

public class UnidadMedida {
    private String clave;
    private String descripcion;

    public UnidadMedida(String clave, String descripcion) {
        this.clave = clave;
        this.descripcion = descripcion;
    }

    public String getClave() {
        return clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return clave + " - " + descripcion;
    }
}
