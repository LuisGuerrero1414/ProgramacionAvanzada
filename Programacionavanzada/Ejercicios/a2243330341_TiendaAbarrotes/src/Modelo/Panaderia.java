package Modelo;

public class Panaderia extends Producto {
    public Panaderia(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Panadería y Tortillería", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Alta rotación diaria";
    }
}