package Modelo;

public class Abarrote extends Producto {
    public Abarrote(int id, String nombre, double precio, String rutaImagen) {
        this(id, nombre, precio, rutaImagen, 10);
    }

    public Abarrote(int id, String nombre, double precio, String rutaImagen, int cantidad) {
        super(id, String.valueOf(id), nombre, precio, 30.0, "Despensa Básica", "", rutaImagen, "pieza", cantidad);
    }

    /** Alta completa: SKU, compra, % ganancia, unidad y stock para semillas o importación. */
    public Abarrote(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia,
            String rutaImagen, String unidadMedida, int cantidad) {
        super(id, sku, nombre, precioCompra, porcentajeGanancia, "Despensa Básica", "", rutaImagen, unidadMedida, cantidad);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "No perecedero (Anaquel)";
    }
}
