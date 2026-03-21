package Modelo;

public class Salchichoneria extends Producto {
    public Salchichoneria(int id, String nombre, double precio, String rutaImagen) {
        super(id, nombre, precio, "Salchichonería", rutaImagen);
    }

    @Override
    public String getTipoAlmacenamiento() {
        return "Refrigerados";
    }
}