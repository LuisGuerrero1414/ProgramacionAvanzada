package Modelo;

public class Abarrote extends Producto {
    public Abarrote(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Abarrotes", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "No perecedero (Anaquel)";
    }
}