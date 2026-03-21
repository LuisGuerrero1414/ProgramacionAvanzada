package Modelo;

public class SnackDulceria extends Producto {
    public SnackDulceria(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Snacks y Dulcería", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Compra por impulso";
    }
}