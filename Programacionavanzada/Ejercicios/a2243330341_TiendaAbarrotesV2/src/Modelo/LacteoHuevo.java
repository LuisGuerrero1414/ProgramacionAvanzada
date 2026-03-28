package Modelo;

public class LacteoHuevo extends Producto {
    public LacteoHuevo(int id, String nombre, double precio, String rutaImagen) {
        this(id, nombre, precio, rutaImagen, 10);
    }

    public LacteoHuevo(int id, String nombre, double precio, String rutaImagen, int cantidad) {
        super(id, String.valueOf(id), nombre, precio, 30.0, "Lácteos y Huevo", "", rutaImagen, "pieza", cantidad);
    }

    public LacteoHuevo(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia,
            String rutaImagen, String unidadMedida, int cantidad) {
        super(id, sku, nombre, precioCompra, porcentajeGanancia, "Lácteos y Huevo", "", rutaImagen, unidadMedida, cantidad);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Cadena de Frío";
    }
}
