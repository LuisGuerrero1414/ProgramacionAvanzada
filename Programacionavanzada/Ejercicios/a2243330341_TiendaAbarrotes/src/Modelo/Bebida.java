package Modelo;

public class Bebida extends Producto {
    public Bebida(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Bebidas", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Líquidos / Pesado";
    }
}