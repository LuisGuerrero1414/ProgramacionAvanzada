package Modelo;

public class Mascota extends Producto {
    public Mascota(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Mascotas", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Volumen / Peso";
    }
}