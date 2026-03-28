package Modelo;

public class FrutaVerdura extends Producto {
    public FrutaVerdura(int id, String nombre, double precio, String rutaImagen) {
        this(id, nombre, precio, rutaImagen, 10);
    }

    public FrutaVerdura(int id, String nombre, double precio, String rutaImagen, int cantidad) {
        super(id, String.valueOf(id), nombre, precio, 30.0, "Frutas y Verduras", "", rutaImagen, "kg", cantidad);
    }

    public FrutaVerdura(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia,
            String rutaImagen, String unidadMedida, int cantidad) {
        super(id, sku, nombre, precioCompra, porcentajeGanancia, "Frutas y Verduras", "", rutaImagen, unidadMedida, cantidad);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Perecedero (Peso variable)";
    }
}
