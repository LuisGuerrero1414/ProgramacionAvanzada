package Modelo;

public class Limpieza extends Producto {
    public Limpieza(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Limpieza del Hogar", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Químicos (Aislados)";
    }
}