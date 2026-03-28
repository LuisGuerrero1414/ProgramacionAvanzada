package Modelo;

public class CuidadoPersonal extends Producto {
    public CuidadoPersonal(int id, String nombre, double precio, String rutaImagen) {
        this(id, nombre, precio, rutaImagen, 10);
    }

    public CuidadoPersonal(int id, String nombre, double precio, String rutaImagen, int cantidad) {
        super(id, String.valueOf(id), nombre, precio, 30.0, "Higiene y Cuidado Personal", "", rutaImagen, "pieza", cantidad);
    }

    public CuidadoPersonal(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia,
            String rutaImagen, String unidadMedida, int cantidad) {
        super(id, sku, nombre, precioCompra, porcentajeGanancia, "Higiene y Cuidado Personal", "", rutaImagen, unidadMedida, cantidad);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Higiene";
    }
}
