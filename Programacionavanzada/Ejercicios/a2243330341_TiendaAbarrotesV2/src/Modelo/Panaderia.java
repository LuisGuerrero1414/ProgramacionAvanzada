package Modelo;

public class Panaderia extends Producto {
    public Panaderia(int id, String nombre, double precio, String rutaImagen) {
        this(id, nombre, precio, rutaImagen, 10);
    }

    public Panaderia(int id, String nombre, double precio, String rutaImagen, int cantidad) {
        super(id, String.valueOf(id), nombre, precio, 30.0, "Alimentos Preparados/Enlatados", "", rutaImagen, "pieza", cantidad);
    }

    public Panaderia(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia,
            String rutaImagen, String unidadMedida, int cantidad) {
        super(id, sku, nombre, precioCompra, porcentajeGanancia, "Alimentos Preparados/Enlatados", "", rutaImagen, unidadMedida, cantidad);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Alta rotación diaria";
    }
}
