package Modelo;

public class Mascota extends Producto {
    public Mascota(int id, String nombre, double precio, String rutaImagen) {
        this(id, nombre, precio, rutaImagen, 10);
    }

    public Mascota(int id, String nombre, double precio, String rutaImagen, int cantidad) {
        super(id, String.valueOf(id), nombre, precio, 30.0, "Mascotas", "", rutaImagen, "pieza", cantidad);
    }

    public Mascota(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia,
            String rutaImagen, String unidadMedida, int cantidad) {
        super(id, sku, nombre, precioCompra, porcentajeGanancia, "Mascotas", "", rutaImagen, unidadMedida, cantidad);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Volumen / Peso";
    }
}
