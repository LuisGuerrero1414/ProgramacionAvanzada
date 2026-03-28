package Modelo;

public class Bebida extends Producto {
    public Bebida(int id, String nombre, double precio, String rutaImagen) {
        this(id, nombre, precio, rutaImagen, 10);
    }

    public Bebida(int id, String nombre, double precio, String rutaImagen, int cantidad) {
        super(id, String.valueOf(id), nombre, precio, 30.0, "Bebidas y Líquidos", "", rutaImagen, "lt", cantidad);
    }

    public Bebida(int id, String sku, String nombre, double precioCompra, double porcentajeGanancia,
            String rutaImagen, String unidadMedida, int cantidad) {
        super(id, sku, nombre, precioCompra, porcentajeGanancia, "Bebidas y Líquidos", "", rutaImagen, unidadMedida, cantidad);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Líquidos / Pesado";
    }
}
